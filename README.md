# Introduction
*Note: AWS infrastructure has been turned off for this application, so the below endpoints no longer work*

Migration of a ticket-booking application to AWS inspired by https://github.com/berndruecker/ticket-booking-camunda-8. <br> <br>
[![Load Test w/Locust](https://github.com/lucgr/ticket-booking/actions/workflows/load_test.yaml/badge.svg)](https://github.com/lucgr/ticket-booking/actions/workflows/load_test.yaml)

# Creating events
Before booking a ticket an event must first be created.

To do this the following command can be used:

```bash
curl -X POST "https://a8meg59qf3.execute-api.eu-central-1.amazonaws.com/events" \
     -H "Content-Type: application/json" \
     -d '{"eventName": "Event","numberOfSeats": 100,"seatPrice": 50}'
```

# Getting events
It's possible to obtain all the events registered in the system. This is needed in order to execute a ticket booking.

To do this the following command can be used:
```bash
curl "https://a8meg59qf3.execute-api.eu-central-1.amazonaws.com/events"
```
# Booking a ticket
Currently, there's two ways to execute the booking. Both cases will need the same JSON input, which relies on using existing eventId and ticketId.

Below is a payload example:
```json
{
    "userId": "User",
    "eventId": "",
    "ticketsIds": [
        ""
    ]
}
```

## Conductor UI
Accessing the [Conductor UI Execution](https://2kihdkj7di.execute-api.eu-central-1.amazonaws.com/workbench). This allows to see the workflow execution step by step.

## Curl
Executing the below call will start an execution.

```bash
curl -X POST "https://p83fxu1m53.execute-api.eu-central-1.amazonaws.com/api/workflow/TicketBooking" \
     -H "Content-Type: application/json" \
     -d '{"userId": "User","eventId": "","ticketsIds": [""]}'
```
To obtain the result of an execution the engine needs to be polled for the current status of the specified workflow, using the returned workflowId from the previous call.

```bash
curl "https://p83fxu1m53.execute-api.eu-central-1.amazonaws.com/api/workflow/{workflowId}?includeTasks=false"
```
