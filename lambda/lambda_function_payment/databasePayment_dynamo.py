from botocore.exceptions import ClientError
from decimal import Decimal

def add_to_data_base(payment_table, data=None):
    try:         
        if not data:
            return 'Error Database', 500
        
        order_id = data.get("orderId")
        if not order_id:
            return 'orderId Missing', 400
        
        data["id"] = order_id

        if "response" in data and "total" in data["response"]:
            data["response"]["total"] = Decimal(str(data["response"]["total"]))
        else:
            print("Payment Error: 'response' or 'total' keys not found\n")
            return 'Error Storing Data', 500

        print("Trying to find orderId: " + order_id + "\n")
        
        response = payment_table.put_item(
            Item=data,
            ConditionExpression='attribute_not_exists(id)'
        )
        print("orderID " + order_id + " found.\n")
        
        if response["ResponseMetadata"]["HTTPStatusCode"] == 200:
            return 'Success', 200
        else:
            return 'Error Storing Data', 500

    except ClientError as e:
        if e.response['Error']['Code'] == 'ConditionalCheckFailedException':
            print("Payment Error: orderID already exists: " + order_id + "\n")
            return 'Already exists', 409
        else:
            return 'Error Storing Data', 500
