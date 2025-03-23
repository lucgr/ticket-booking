Migration of a ticket-booking application to AWS inspired by https://github.com/berndruecker/ticket-booking-camunda-8.

## Instructions for running the app:
TODO...





## Useful features for rabbitMQ

View queue messages:
docker exec -it rabbitmq bash
rabbitmqctl list_queues

## Information about ticket payment

Final version used in AWS is located in `lambda/lambda_function_payment` (Uses RabbitMQ and DynamoDB)
Initial local version used for debbugging, located in: `/payment_service` (Uses RabbitMQ and MongoDB)
