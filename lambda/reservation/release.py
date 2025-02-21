import simplejson as json
import boto3

class OrderNotFoundException(Exception):
    pass

dynamodb = boto3.resource("dynamodb")
order_table = dynamodb.Table('orders')
ticket_table = dynamodb.Table('tickets')


def lambda_handler(event, context):
    body = {}
    body['code'] = 200

    try:
        order_id = event['pathParameters']['orderId']
        order_response = order_table.get_item(Key={'id': order_id})
        
        if 'Item' not in order_response:
            raise OrderNotFoundException(f"Order {order_id} not found")

        order = order_response['Item']
        ticket_ids = order['ticketIds']
        event_id = order['eventId']

        for ticket_id in ticket_ids:
            ticket_table.update_item(
                Key={'id': ticket_id, 'eventId': event_id },
                UpdateExpression="set availability = :a",
                ExpressionAttributeValues={':a': 'AVAILABLE'}
            )

        body['message'] = 'Tickets successfully released'
        body['orderId'] = order_id
    except KeyError:
        body['code'] = 400
        body['message'] = 'Invalid request'
    except OrderNotFoundException as e:
        body['code'] = 404
        body['message'] = str(e)
    except Exception as e:
        body['code'] = 500
        body['message'] = str(e)

    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps(body)
    }
