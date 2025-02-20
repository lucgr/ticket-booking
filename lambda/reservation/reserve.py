import simplejson as json
import boto3
from decimal import Decimal

class TicketNotAvailableException(Exception):
    pass

dynamodb = boto3.resource("dynamodb")
order_table = dynamodb.Table('orders')
ticket_table = dynamodb.Table('tickets')

def lambda_handler(event, context):
    body = {}
    body['code'] = 200

    try:
        requestJSON = json.loads(event['body'])
        order_id = requestJSON['orderId']
        user_id = requestJSON['userId']
        event_id = requestJSON['eventId']
        ticket_ids = requestJSON['ticketIds']

        available_tickets = []
        for ticket_id in ticket_ids:
            response = ticket_table.get_item(Key={'id': ticket_id, 'eventId': event_id})
            if 'Item' in response and response['Item']['availability'] == 'AVAILABLE':
                available_tickets.append(response['Item'])
            else:
                raise TicketNotAvailableException(f"Ticket {ticket_id} is not available")

        for ticket in available_tickets:
            ticket_table.update_item(
                Key={'id': ticket['id'], 'eventId': ticket['eventId']},
                UpdateExpression="set availability = :a",
                ExpressionAttributeValues={':a': 'RESERVED'}
            )

        order_table.put_item(
            Item={
                'id': order_id,
                'userId': user_id,
                'eventId': event_id,
                'ticketIds': ticket_ids
            })
        for ticket in available_tickets:
            ticket['availability'] = 'RESERVED'
        
        body['message'] = 'Tickets reserved successfully'
        body['orderId'] = order_id
        body['tickets'] = available_tickets
    except TicketNotAvailableException as e:
        body['code'] = 409
        body['orderId'] = order_id
        body['message'] = str(e)
    except KeyError:
        body['code'] = 400
        body['message'] = 'Invalid request'
    except Exception as e:
        body['code'] = 500
        body['message'] = "WTFFF"+str(e)

    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps(body)
    }
