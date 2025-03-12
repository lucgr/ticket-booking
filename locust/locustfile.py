import json
from queue import Queue
import random
import uuid
from locust import HttpUser, events, task, between
import time

import requests
from requests.adapters import HTTPAdapter
from urllib3 import Retry

# ticket_queue = Queue()
# event_id = "2a65496b-1d10-4ddf-a632-cd401b7ca6e0"

API_HOST = "https://a8meg59qf3.execute-api.eu-central-1.amazonaws.com"

# @events.test_start.add_listener
# def populate_tickets(environment, **kwargs):
#     global event_id
#     # ticket_queue.queue.clear()
    
#     try:
#         # Create event with 1000 tickets
#         timestamp = str(int(time.time()))
#         create_response = requests.post(
#             f"{API_HOST}/events",
#             json={
#                 "eventName": f"LoadTestEvent-{timestamp}",
#                 "numberOfSeats": 1000,
#                 "seatPrice": 100
#             },
#             headers={
#                 "accept": "application/json",
#                 "Content-Type": "application/json"
#             },
#             timeout=10
#         )
#         create_response.raise_for_status()
#         event_id = create_response.json()["eventId"]

#         # # Retrieve tickets
#         # get_response = requests.get(
#         #     f"{API_HOST}/events",
#         #     headers={
#         #         "accept": "application/json",
#         #         "Content-Type": "application/json"
#         #     },
#         #     timeout=30
#         # )
#         # get_response.raise_for_status()
        
#         # events_data = get_response.json()
#         # target_event = next((e for e in events_data if e["eventId"] == event_id), None)
        
#         # if not target_event:
#         #     raise ValueError(f"Event {event_id} not found in API response")
        
#         # ticket_ids = target_event.get("ticketIds", [])
#         # if len(ticket_ids) != 1000:
#         #     raise ValueError(f"Expected 1000 tickets, got {len(ticket_ids)}")

#         # for ticket_id in ticket_ids:
#         #     ticket_queue.put(ticket_id)

#         # print(f"Successfully created event {event_id} with {ticket_queue.qsize()} tickets")

#     except Exception as e:
#         print(f"Error during ticket population: {str(e)}")
#         environment.events.request.fire(
#             request_type="SETUP",
#             name="Ticket Population",
#             response_time=0,
#             response_length=0,
#             exception=e,
#             context=None
#         )
#         environment.runner.quit()

class WorkflowUser(HttpUser):
    global API_HOST
    wait_time = between(1, 5) # Wait between 1 and 5 seconds between tasks to simulate user think time
    # event_id = "2a65496b-1d10-4ddf-a632-cd401b7ca6e0"
    
    @task
    def trigger_workflow(self):

        get_response = requests.get(
            f"{API_HOST}/events",
            headers={
                "Content-Type": "application/json"
            },
            timeout=30
        )
        get_response.raise_for_status()
        
        events_data = get_response.json()
        
        event_id = events_data.get("events")[0].get("id")
        ticket_ids = events_data.get("events")[0].get("availableTicketIds")
        
        if not ticket_ids:
            raise ValueError(f"No tickets found for event {event_id}")
        
        ticket_id = random.choice(ticket_ids)
            

        headers = {"Content-Type": "application/json"}
        payload = {
            "mode": "raw",
            "raw": json.dumps({
                "userId": f"User-{uuid.uuid4()}",  # Optional unique user ID
                "eventId": event_id,
                "ticketsIds": [ticket_id]
            }),
            "options": {
                "raw": {
                    "language": "json"
                }
            }
        }
        
        start_time = time.time()
        
        # Send POST request
        with self.client.post(
            "/api/workflow/TicketBooking",
            json=payload,
            headers=headers,
            catch_response=True
        ) as response:
            latency = (time.time() - start_time) * 1000  # Convert to ms
            
            # Track custom metrics
            if response.status_code == 200:
                response.success()
                self.environment.events.request.fire(
                    request_type="POST",
                    name="TriggerWorkflow",
                    response_time=latency,
                    response_length=len(response.content),
                    exception=None,
                )
            else:
                response.failure(f"Status {response.status_code}")