import json
import boto3
import base64
import random
import pika
import ssl
import time
import os
from databasePayment_dynamo import add_to_data_base

dynamodb = boto3.resource("dynamodb")
payment_table = dynamodb.Table('payments')
RABBITMQ_HOST = "b-ca3ca3c4-bacd-4115-a69f-9017031e916d.mq.eu-central-1.amazonaws.com"

def rabbitmq_connection(retry_delay=5, maxRetries=10):
    retries = 0
    while retries < maxRetries:
        try:
            context = ssl.create_default_context(cafile='/opt/keys/AmazonRootCA1.pem')
            context.check_hostname = True

            ssl_options = pika.SSLOptions(context=context, server_hostname=RABBITMQ_HOST)

            print(f"Trying to connect to rabbitMQ (attempt {retries + 1})...", flush=True)
            credentials = pika.PlainCredentials("conductor", "conductor123456")
            connection = pika.BlockingConnection(pika.ConnectionParameters(RABBITMQ_HOST, '5671', '/', credentials, ssl_options=ssl_options))
            channel = connection.channel()
            print("Connected to rabbitmq", flush=True)
            return connection, channel
        except pika.exceptions.AMQPConnectionError:
            print(f"Connection failed...", flush=True)
            retries += 1
            time.sleep(retry_delay)
    raise Exception(f"Could not connect to rabbitMQ attempts")


def simulate_payment():
    resultList = ["success", "fail", "delay"]
    result = random.choices(resultList, weights=[0.8, 0.15, 0.05])[0]
    # Uncomment if simulation delay is needed
    if result == "delay":
        # time.sleep(random.randint(3, 6))
        return result, 202
    elif result == "fail":
        return result, 400
    return result, 200

def process_payment_request(message):
    
    try:
    
        # time.sleep(5) # Testing purposes
        #message = json.loads(body.decode())

        total = 0
        for ticket in message.get("body", {}).get("tickets", []):
            total += ticket.get('price', 0)
        
        orderId = message.get('body', {}).get('orderId', 'Unknown Order')

        print(f"({orderId}) Payment request is received, total: {total} euros, please enter payment details..", flush=True)
        
        result, status_code = simulate_payment() # Simulation of payment, returns success, fail or timeout.
            
        print(f"({orderId}) Payment status: '{result}', Status code: {status_code}", flush=True)
        print(f"({orderId}) Storing to DB...", flush=True)
        response = {
            "orderId": orderId,
            "code": status_code,
            "message": "Payment: " + result,
            "response": {
                "total": total,
                "status": result
            }
        } # Ideally this will be stored in the table
        
        status_code_db = 200
        resultDB, status_code_db = add_to_data_base(payment_table, response)
        
        if(status_code_db != 200):
            response = {
                "orderId": orderId,
                "code": status_code_db,
                "message": "Payment: " + result + "Database: " + resultDB ,
                "response": {
                    "total": total,
                    "status": result
            }
        }
        
        print(f"({orderId}) Database status: '{resultDB}', Return code: {status_code_db}", flush=True)
        print(f"({orderId}) Sending response back...", flush=True)
        
        connection, channel = rabbitmq_connection()
        properties = pika.BasicProperties(message_id=orderId,correlation_id=orderId, delivery_mode=2,content_type="application/json", content_encoding="UTF-8")
        channel.basic_publish(exchange='', routing_key="payment_response", body=json.dumps(response), properties=properties)
        connection.close() 
        

        print(f"({orderId}) Send finish!", flush=True)
        
        return response
    except Exception as e:
        print(f"Error processing payment request: {e}", flush=True)
        raise



def lambda_handler(event, context):
    try:
        response_body = {
            'message': 'Hello from Lambda!',
            'event': event
        }
        # h
        event_data = event['rmqMessagesByQueue'].get('payment_request::/', [])[0].get('data', '')

        decoded_bytes = base64.b64decode(event_data)
        decoded_event_data = json.loads(decoded_bytes.decode('utf-8'))
        # print(os.listdir('/opt/payment_service/python/'))
        # print("Contents of /opt/keys:", os.listdir('/opt/keys'))
        
        process_payment_request(decoded_event_data)
        
        return {
            'statusCode': 200,
            'body': json.dumps(response_body)
        }
    except Exception as e:
        print(f"Error in lambda_handler: {e}", flush=True)
        return {
            'statusCode': 500,
            'body': json.dumps({'error': str(e)})
        }
