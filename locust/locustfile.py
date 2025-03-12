import json
import random
import uuid
from locust import HttpUser, task
import time

import requests

API_HOST = "https://a8meg59qf3.execute-api.eu-central-1.amazonaws.com"

class WorkflowUser(HttpUser):
    global API_HOST
    
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

        event_id = None
        ticket_ids = None
        
        for event in events_data.get("events"):
            if event.get("availableTicketIds"):
                event_id = event.get("id")
                ticket_ids = event.get("availableTicketIds")
                break
        else:
            raise ValueError("No events with available tickets found")
        
        if not ticket_ids:
            raise ValueError("No tickets found")
        
        ticket_id = random.choice(ticket_ids)
            

        headers = {"Content-Type": "application/json"}
        payload = {
            "mode": "raw",
            "raw": json.dumps({
                "userId": f"User-{uuid.uuid4()}", 
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