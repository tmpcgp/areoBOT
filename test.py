import requests
import json
from dotenv import load_dotenv
import os

load_dotenv()  # take environment variables from .env.

ID    = 0
URL   = "void(url)"
TOKEN = "fkskfjsfkjsfs"
NAME  = "Hello"
SPEC  = "Some spec"
HASH  = "284fsklfjs"
IDC   = 0

def mock_create_account():
    print("@mock_create_account")
    
    global ID

    account  = {
        "name": NAME,
        "spec": SPEC,
        "key" : HASH
    };

    url = "http://localhost:5000/account/create";
    response = requests.post(url,json=account)
    ID       = 1

    print(response.content)
    print("@mock_create_account [end]")


def mock_auth_account():
    print("@mock_auth_account")

    input_name = NAME
    input_key  = HASH

    input_account = {
        "name" : input_name,
        "key"  : input_key
    };

    url = "http://localhost:5000/auth"
    response = requests.post(url,json=input_account)

    # this endpoint doesn't return a json object,
    # we must read the bytes.
    print(response.content)
    print("@mock_auth_account [end]")

def mock_update_account():
    print("@mock_update_account")

    naccount = {
        "spec" : "somethign new"
    };

    url = "http://localhost:5000/account/update?id="+str(ID)
    response = requests.put(url,json=naccount)
    
    print(response.status_code)
    print(response.content)
    print("@mock_update_account [end]")



def mock_create_config():
    print("@mock_create_config")

    global IDC
    input_config = {
        "name" : "somenameconfig",
        "states" : [],
        "intents": []
    };

    url      = "http://localhost:5000/config/create?id="+str(ID)
    response = requests.post(url,json=input_config)
    IDC      = response.json()["id"]

    print(response.json())
    print("@mock_create_config [end]")





## @Incomplete to test more.
def mock_udpate_config():
    print("mock_update_config")

    global IDC
    nstates = []
    nchoices= []
    nchoice = {
        "name": "fksjfsj"
    };
    nchoices.append(nchoice)
    nstate  = {
        "name"   : "somestatename",
        "answers": ["klsfj","isfsfks","fkjsfjs"],
        "choices": nchoices
    };

    nstates.append(nstate)
    input_config = {
        "name" : "somenewnameconfig",
        "states" : nstates
    };

    url = "http://localhost:5000/config/update?id="+str(IDC)
    response = requests.put(url,json=input_config)

    print(response.content)
    print("mock_update_config [end]")






## @Incomplete to test more.
def mock_config_delete():
    print("@mock_config_delete")

    global IDC
    url      = "http://localhost:5000/config/delete?id="+str(IDC)
    response = requests.get(url)

    print(response.content)
    print("@mock_config_delete [end]")






def mock_get_all_configs():
    print("@mock_get_all_configs")

    global ID
    url = "http://localhost:5000/config/all?id="+str(ID)
    response = requests.get(url)

    print(response.content)
    print("@mock_get_all_configs [end]")

def mock_account_delete():
    print("@mock_account_delete")

    global ID
    response = requests.delete(URL+"/"+str(ID)+"?api_key="+TOKEN)

    print(response.content)
    print("@mock_account_delete [end]")

if __name__ == "__main__":

    mock_create_account()
    print("\n")
    mock_auth_account()
    print("\n")
    mock_update_account()
    print("\n")
    mock_create_config()
    print("\n")
    mock_udpate_config()
    print("\n")
    mock_get_all_configs()
    print("\n")
    mock_config_delete()

    """
    print("\n")
    mock_get_all_accounts()
    mock_config_delete()
    print("\n")
    print("\n")
    print("\n")
    print("\n")
    print("\n")
    print("\n")
    print("\n")
    mock_account_delete()
    print("\n")
    """
