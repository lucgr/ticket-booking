import os
import pymongo
import json

def add_to_data_base(data=None):
    if not data:
        return 'Error Database', 400
    
    mongo_uri = os.getenv("MONGO_URI", "mongodb://mongo:27017/")
    myclient = pymongo.MongoClient(mongo_uri)
    payment_database = myclient["payDB"]
    payment_invoices = payment_database["invoices"]
    
    exists = payment_invoices.find_one({"_id": data.get("orderId")})
    if exists:
        return 'Already exists', 409
    
    dict_data = data
    dict_data["_id"] = dict_data.pop("orderId")
    
    payment_invoices.insert_one(dict_data)
    return 'Success', 200





