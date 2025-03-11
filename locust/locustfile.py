from locust import HttpUser, task, between
import time

class WorkflowUser(HttpUser):
    wait_time = between(1, 5) # Wait between 1 and 5 seconds between tasks to simulate user think time
    
    @task
    def trigger_workflow(self):
        headers = {"Content-Type": "application/json"}
        payload = {
            "mode": "raw",
            "raw": "{\n  \"userId\": \"User\",\n  \"eventId\": \"dc80ace0-497c-49c5-b4b6-7056856fc731\",\n  \"ticketsIds\": [\n    \"fe8a91e6-363a-4b55-9168-e5c9772e37ae\"\n  ]\n}",
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