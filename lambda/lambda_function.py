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
BUCKET_NAME = os.environ.get('S3_TICKET_BUCKET') # Should resolve to "ticket-pdfs-devops"

def lambda_handler(event, context):
    logger.info("Parsing input data...")
    try:
        body = event.get('body')
        if isinstance(body, str):
            data = json.loads(body)
        else:
            data = body
        customer_name = data['customer_name']
        events = data['events'] # Expected to be a list of dicts with keys: ticket_id, price etc.
        # tickets = data['tickets']  # Expected to be a list of dicts with keys: ticket_id, event_id, price etc.
    except Exception as e:
        return {
            'statusCode': 400,
            'body': json.dumps({'error': 'Invalid input', 'details': str(e)})
        }
    
    # Create a temporary PDF file in /tmp (other directories arent writable through lambda)
    temp_pdf = tempfile.NamedTemporaryFile(delete=False, suffix=".pdf")
    pdf_path = temp_pdf.name
    temp_pdf.close()

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
    for event in events:
        tickets = event.get('tickets')
        c.drawString(50, y, f"Event: {event}")
        y -= 40
        for ticket in tickets:
            ticket_id = ticket.get('ticket_id')
            price = ticket.get('price')

            # Writing ticket details to the PDF
            c.setFont("Helvetica", 12)
            c.drawString(50, y, f"Ticket ID: {ticket_id} | Price: {price}")
            y -= 20

            # Generate QR code for ticket_id and saving to temp-file
            qr_img = qrcode.make(ticket_id)
            temp_qr = tempfile.NamedTemporaryFile(delete=False, suffix=".png")
            qr_img.save(temp_qr.name)
            temp_qr.close()

            # Drawing the QR code image onto the PDF (adjusting position and size as needed)
            c.drawImage(temp_qr.name, 400, y - 20, width=100, height=100)
            y -= 120
            os.remove(temp_qr.name)

            # If there isn’t enough space on the current page, start a new page
            if y < 150:
                c.showPage()
                y = height - 50
    c.save()

    # Defining the S3 object key (file path to store the ticket in the bucket)
    logger.info("Generating ticket path...")
    pdf_key = f"tickets/{customer_name.replace(' ', '_')}_{context.aws_request_id}.pdf"

    try:
        # Upload the PDF to S3
        logger.info("Uploading PDF to S3...")
        s3.upload_file(pdf_path, BUCKET_NAME, pdf_key, ExtraArgs={'ContentType': 'application/pdf'})
    except Exception as e:
        logger.error(f"Failed to upload PDF to S3: {e}")
        return {
            'statusCode': 500,
            'body': json.dumps({'error': 'Upload failed', 'details': str(e)})
        }
    finally:
        os.remove(pdf_path)

    # Generate a presigned URL to access the PDF from S3 (only valid for 1 hour)
    try:
        presigned_url = s3.generate_presigned_url('get_object', Params={'Bucket': BUCKET_NAME, 'Key': pdf_key}, ExpiresIn=3600)
    except Exception as e:
        logger.error(f"Failed to generate presigned URL: {e}")
        return {
            'statusCode': 500,
            'body': json.dumps({'error': 'Failed to generate URL', 'details': str(e)})
        }
    logger.info("Ticket generation successful! Returning presigned URL...")
    return {
        'statusCode': 200,
        'body': json.dumps({'url': presigned_url})
    }
