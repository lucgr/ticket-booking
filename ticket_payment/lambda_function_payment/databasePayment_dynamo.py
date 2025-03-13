def add_to_data_base(payment_table, data=None):

    print("Step 1\n")
    

    if not data:
        return 'Error Database', 400
    
    print("Step 2\n")
    order_id = data.get("orderId")
    if not order_id:
        return 'orderId Missing', 400
    
    print("Step 3\n")
    print("Trying to find orderId: " + order_id + "\n")
    existing_item = payment_table.get_item(Key={"id": order_id})
    print("Step 3.5\n")
    if "Item" in existing_item:
        return 'Already exists', 409
    
    print("Step 4\n")
    data["id"] = order_id
    response = payment_table.put_item(Item=data)
    
    print("Step 5\n")
    if response["ResponseMetadata"]["HTTPStatusCode"] == 200:
        return 'Success', 200
    else:
        return 'Error Storing Data', 500