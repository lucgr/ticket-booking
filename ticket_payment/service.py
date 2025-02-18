import time
import pika
import random
import json

# Specify the name of the IP address

RABBITMQ_HOST = "rabbitmq" 
#RABBITMQ_HOST = "localhost" 

INPUT_QUEUE_NAME = "payment_queue"
OUTPUT_QUEUE_NAME = "payment_response_queue"


def rabbitmq_connection(retry_delay=5, max_retries=10):
    retries = 0
    while retries < max_retries:
        try:
            print(f"Trying to connect to RabbitMQ (attempt {retries + 1})...", flush=True)
            connection = pika.BlockingConnection(pika.ConnectionParameters(RABBITMQ_HOST))
            channel = connection.channel()
            channel.queue_declare(queue= INPUT_QUEUE_NAME)
            print("Connected to rabbitmq", flush=True)
            return connection, channel
        except pika.exceptions.AMQPConnectionError:
            print(f"Connection failed...", flush=True)
            retries += 1
            time.sleep(retry_delay)
    raise Exception(f"Could not connect to RabbitMQ after {max_retries} attempts.")


def simulate_payment():
    resultList = ["success", "fail", "delay"]
    result = random.choices(resultList, weights=[0.8, 0.15, 0.05])[0]
    # Uncomment if simulation delay is needed
    if result == "delayed":
        # time.sleep(random.randint(3, 6))
        return result, 202
    elif result == "failure":
        return result, 400
    return result, 200


def process_payment_request(ch, method, properties, body):
    
    message = json.loads(body.decode())
    
    total = 0
    for ticket in message.get("tickets", []):
        total += ticket.get('price', 0)
    
    print(f"Payment request is received from {message['request_id']}, total: {total} euros, please enter payment details..", flush=True)
    
    result, status_code = simulate_payment() # Simulation of payment, returns success, fail or timeout.
        
    print(f"Payment was: {result} for request: {message['request_id']}, Status code: {status_code}", flush=True)
    
    response = {
        "request_id": message["request_id"],
        "status": result,
        "status_code": status_code,
        "total_amount": total
    } # Ideally this will be stored in the table
    
    print(f"Sending response back: {message['request_id']}", flush=True)
    connection, channel = rabbitmq_connection()
    channel.queue_declare(queue=OUTPUT_QUEUE_NAME)
    channel.basic_publish(exchange='', routing_key=OUTPUT_QUEUE_NAME, body=json.dumps(response))
    connection.close()
    print(f"Send finish: {message['request_id']}", flush=True)

    ch.basic_ack(delivery_tag=method.delivery_tag)
    
    return response


# Used for sending message to the input queue
def send_payment_request(request_id: str, customer_id: str, tickets: list):
    connection, channel = rabbitmq_connection()
    
    message = {
        "request_id": request_id,
        "customer_id": customer_id,
        "tickets": tickets
    }
    
    channel.basic_publish(exchange='', routing_key=INPUT_QUEUE_NAME, body=json.dumps(message))
    connection.close()
    print(f"Payment request for order {request_id} sent to queue", flush=True)


def starts_server():
    print("Trying to set up connection\n", flush=True)
    connection, channel = rabbitmq_connection()
    channel.basic_consume(queue=INPUT_QUEUE_NAME, on_message_callback=process_payment_request)
    print("Payment service is listening for messages...", flush=True)
    channel.start_consuming()
    
    
if __name__ == "__main__":
    starts_server()



# Source: https://www.rabbitmq.com/tutorials/tutorial-one-python