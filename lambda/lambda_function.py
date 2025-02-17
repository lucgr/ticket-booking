import json
import os
import tempfile
import boto3
import qrcode
import logging
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas


logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)
logger.info("Initializing S3 client...")
s3 = boto3.client('s3')
# The S3 bucket name will be provided as an environment variable eventually
# BUCKET_NAME = os.environ.get('BUCKET_NAME')
BUCKET_NAME = "ticket-pdfs-devops"

def lambda_handler(event, context):
    logger.info("Parsing input data...")
    try:
        body = event.get('body')
        if isinstance(body, str):
            data = json.loads(body)
        else:
            data = body

        customer_name = data['customer_name']
        tickets = data['tickets']  # Expected to be a list of dicts with keys: ticket_id, event_id, price
    except Exception as e:
        return {
            'statusCode': 400,
            'body': json.dumps({'error': 'Invalid input', 'details': str(e)})
        }
    
    # Create a temporary PDF file in the /tmp directory (the only writable directory in Lambda)
    temp_pdf = tempfile.NamedTemporaryFile(delete=False, suffix=".pdf")
    pdf_path = temp_pdf.name
    temp_pdf.close()  # We'll write using ReportLab

    # Initialize the PDF canvas
    c = canvas.Canvas(pdf_path, pagesize=letter)
    width, height = letter
    y = height - 50

    # Add customer information
    c.setFont("Helvetica-Bold", 16)
    c.drawString(50, y, f"Ticket for: {customer_name}")
    y -= 40

    # Process each ticket
    logger.info("Generating ticket PDFs...")
    for ticket in tickets:
        ticket_id = ticket.get('ticket_id')
        event_id = ticket.get('event_id')
        price = ticket.get('price')

        # Write ticket details to the PDF
        c.setFont("Helvetica", 12)
        c.drawString(50, y, f"Ticket ID: {ticket_id} | Event ID: {event_id} | Price: {price}")
        y -= 20

        # Generate QR code for ticket_id
        qr_img = qrcode.make(ticket_id)
        # Save QR code image to a temporary file
        temp_qr = tempfile.NamedTemporaryFile(delete=False, suffix=".png")
        qr_img.save(temp_qr.name)
        temp_qr.close()

        # Draw the QR code image onto the PDF (adjust position and size as needed)
        c.drawImage(temp_qr.name, 400, y - 20, width=100, height=100)
        y -= 120

        # Remove the temporary QR code image
        os.remove(temp_qr.name)

        # If there isn’t enough space on the current page, start a new page
        if y < 150:
            c.showPage()
            y = height - 50

    c.save()

    # Define the S3 object key (file path in the bucket)
    logger.info("Generating ticket path...")
    pdf_key = f"tickets/{customer_name.replace(' ', '_')}_{context.aws_request_id}.pdf"

    try:
        # Upload the PDF to S3
        logger.info("Uploading PDF to S3...")
        s3.upload_file(pdf_path, BUCKET_NAME, pdf_key, ExtraArgs={'ContentType': 'application/pdf'})
    except Exception as e:
        return {
            'statusCode': 500,
            'body': json.dumps({'error': 'Upload failed', 'details': str(e)})
        }
    finally:
        # Clean up the temporary PDF file
        os.remove(pdf_path)

    # Generate a presigned URL (valid for 1 hour) to access the PDF
    try:
        presigned_url = s3.generate_presigned_url('get_object',
                                                  Params={'Bucket': BUCKET_NAME, 'Key': pdf_key},
                                                  ExpiresIn=3600)
    except Exception as e:
        return {
            'statusCode': 500,
            'body': json.dumps({'error': 'Failed to generate URL', 'details': str(e)})
        }

    return {
        'statusCode': 200,
        'body': json.dumps({'url': presigned_url})
    }
