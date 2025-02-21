import simplejson as json
import boto3
from boto3.dynamodb.conditions import Key


dynamodb = boto3.resource('dynamodb')
events_table = dynamodb.Table('events')
tickets_table = dynamodb.Table('tickets')

def lambda_handler(event, context):
    # Fetch first 5 events
    body = {}

    event_response = events_table.scan(Limit=5)
    events = event_response['Items']
    
    # Fetch tickets for each event
    for event_item in events:
        event_id = event_item['id']
        ticket_response = tickets_table.query(
            IndexName='eventId-index',
            KeyConditionExpression=Key('eventId').eq(event_id)
        )
        ticket_ids = [ticket['id'] for ticket in ticket_response['Items'] if ticket["availability"] == "AVAILABLE"]
        event_item['availableTicketIds'] = ticket_ids

        body['code'] = 200
        body['message'] = 'Events fetched successfully'
        body['events'] = events
    
    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps(body)
    }
