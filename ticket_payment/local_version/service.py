import time
import pika
import random
import json
import os
from multiprocessing import Process

from databasePayment import add_to_data_base

# Specify the name of the IP address

RABBITMQ_HOST = "rabbitmq" 
#RABBITMQ_HOST = "localhost" 

mongo_username = os.getenv("RABBITMQ_DEFAULT_USER", "user")
mongo_password = os.getenv("RABBITMQ_DEFAULT_PASS", "password")

INPUT_QUEUE_NAME = "payment_queue"
OUTPUT_QUEUE_NAME = "payment_response_queue"


def rabbitmq_connection(retry_delay=5, maxRetries=10):
    retries = 0
    while retries < maxRetries:
        try:
            print(f"Trying to connect to rabbitMQ (attempt {retries + 1})...", flush=True)
            credentials = pika.PlainCredentials(mongo_username, mongo_password)
            connection = pika.BlockingConnection(pika.ConnectionParameters(RABBITMQ_HOST, '5672', '/', credentials))
            channel = connection.channel()
            channel.queue_declare(queue= INPUT_QUEUE_NAME, durable=True)
            channel.queue_declare(queue=OUTPUT_QUEUE_NAME, durable=True)
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


def process_payment_request(ch, method, properties, body):
    # time.sleep(5) # Testing purposes
    message = json.loads(body.decode())

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
    
    resultDB, status_code_db = add_to_data_base(response)
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
    channel.basic_publish(exchange='', routing_key=OUTPUT_QUEUE_NAME, body=json.dumps(response), properties=properties)
    connection.close()
    
    print(f"({orderId}) Send finish!", flush=True)

    ch.basic_ack(delivery_tag=method.delivery_tag)
    
    return response


# Used for sending message to the input queue (debugging purposes)
def send_payment_request(request_id: str = "2908beb8-1743-4c4e-80d8-4daf8e8a7b4c"):
    connection, channel = rabbitmq_connection()
    
    message = {
        "event_produced": "amqp_queue:conductor",
        "asyncComplete": False,
        "sink": "amqp_queue:conductor",
        "workflowType": "TicketBooking",
        "correlationId": None,
        "taskToDomain": {},
        "workflowVersion": 24,
        "body": {
            "eventId": "11dfd9b3-a0bf-476d-9438-16c7683e524d",
            "tickets": [
            {
                "ticketId": "1",
                "eventId": "11dfd9b3-a0bf-476d-9438-16c7683e524d",
                "availability": "TICKET_AVAILABILITY_RESERVED",
                "price": 100.15
            },
            {
                "ticketId": "2",
                "eventId": "11dfd9b3-a0bf-476d-9438-16c7683e524d",
                "availability": "TICKET_AVAILABILITY_RESERVED",
                "price": 100.15
            },
            {
                "ticketId": "3",
                "eventId": "11dfd9b3-a0bf-476d-9438-16c7683e524d",
                "availability": "TICKET_AVAILABILITY_RESERVED",
                "price": 100.15
            }
            ],
            "orderId": request_id,
            "userId": "test"
        },
        "workflowInstanceId": "2908beb8-1743-4c4e-80d8-4daf8e8a7b4c"
    }
    
    channel.basic_publish(exchange='', routing_key=INPUT_QUEUE_NAME, body=json.dumps(message))
    connection.close()
    print(f"({message.get('body', {}).get('orderId', 'Unknown Order')})Payment request for order sent to queue...", flush=True)


def create_new_process(ch, method, properties, body):
    p = Process(target=process_payment_request, args=(ch, method, properties, body))
    p.start()
    print("Extra consumer process spawned with PID:", p.pid, flush=True)

def starts_server():
    print("Trying to set up connection\n", flush=True)
    connection, channel = rabbitmq_connection()
    channel.basic_consume(queue=INPUT_QUEUE_NAME, on_message_callback=create_new_process)
    print("Payment service is listening for messages...", flush=True)
    channel.start_consuming()
    
    
if __name__ == "__main__":
    starts_server()



# Source: https://www.rabbitmq.com/tutorials/tutorial-one-python