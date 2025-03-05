import simplejson as json
import boto3
import uuid

dynamodb = boto3.resource("dynamodb")
event_table = dynamodb.Table('events')
ticket_table = dynamodb.Table('tickets')

def lambda_handler(event, context):
    body = {}
    body['code'] = 200

    try:
        requestJSON = json.loads(event['body'])
        event_id = str(uuid.uuid4())
        # Create event
        event_table.put_item(
            Item={
                'id': event_id,
                'eventName': requestJSON['eventName'],
                'numberOfSeats': requestJSON['numberOfSeats'],
                'seatPrice': int(requestJSON['seatPrice'])
            })
        
        # Create tickets for the event
        for seat_number in range(1, requestJSON['numberOfSeats'] + 1):
            # ticket_id = str(uuid.uuid4())
            ticket_table.put_item(
                Item={
                    'id': str(uuid.uuid4()),
                    'eventId': event_id,
                    'seatNumber': seat_number,
                    'price': int(requestJSON['seatPrice']),
                    'availability': 'AVAILABLE'
                })
        
        body['message'] = 'Event created successfully'
        body['eventId'] = event_id
    except KeyError:
        body['code'] = 400
        body['message'] = 'Invalid request'
    except Exception as e:
        body['code'] = 500
        body['message'] = 'Internal server error: ' + str(e)
    
    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps(body)
    }
