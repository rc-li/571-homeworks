from flask import Flask
# from app import app
import requests
import json
app = Flask(__name__)

# need to
# 1. parse URL that client sent
# 2. contruct URL to send to eBay
# 3. parse eBay response
# 4. send back to client

@app.route('/')
def hello():
    return "hello"

    # resp = requests.get('https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=RayLi-exp-PRD-d2eb6beb6-1a4f60ed&RESPONSE-DATA-FORMAT=JSON&keywords=harry%20potter')
    # resp = requests.get('https://svcs.ebay.com/services/search/FindingService/v1?'
    # 'OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0'
    # '&SECURITY-APPNAME=RayLi-exp-PRD-d2eb6beb6-1a4f60ed&RESPONSE-DATA-FORMAT=JSON'
    # '&keywords=harry%20potter&sortOrder=PricePlusShippingLowest')


    
    # resp = requests.get('https://google.com')
    # return resp.content
    # res={"data":resp}
    # response=json.dumps(res)
    
    # return response


    # resp = requests.get('https://todolist.example.com/tasks/')
    # if resp.status_code != 200:
    #     # This means something went wrong.
    #     raise ApiError('GET /tasks/ {}'.format(resp.status_code))
    # for todo_item in resp.json():
    #     print('{} {}'.format(todo_item['id'], todo_item['summary']))
    # return "busteria"


if __name__ == "__main__":
    app.run()
