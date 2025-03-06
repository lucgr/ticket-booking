def add_to_data_base(payment_table, data=None):
    if not data:
        return 'Error Database', 400
    
    order_id = data.get("orderId")
    if not order_id:
        return 'orderId Missing', 400
    
    existing_item = payment_table.get_item(Key={"id": order_id})
    if "Item" in existing_item:
        return 'Already exists', 409
    
    data["id"] = order_id
    response = payment_table.put_item(Item=data)
    
    if response["ResponseMetadata"]["HTTPStatusCode"] == 200:
        return 'Success', 200
    else:
        return 'Error Storing Data', 500