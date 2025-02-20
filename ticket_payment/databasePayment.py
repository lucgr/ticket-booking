import os
import pymongo
import json

def addToDataBase(data=None):
    if not data:
        return 'Error Database', 400
    
    mongo_uri = os.getenv("MONGO_URI", "mongodb://mongo:27017/")
    myclient = pymongo.MongoClient(mongo_uri)
    paymentDatabase = myclient["payDB"]
    payment_invoices = paymentDatabase["students"]
    
    exists = payment_invoices.find_one({"_id": data.get("orderId")})
    if exists:
        return 'Already exists', 409
    
    dictData = data
    dictData["_id"] = dictData.pop("orderId")
    
    payment_invoices.insert_one(dictData)
    return 'Success', 200





