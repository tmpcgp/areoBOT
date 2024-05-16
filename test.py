import requests
import json
from dotenv import load_dotenv
import os

load_dotenv()  # take environment variables from .env.

ID    = 0
URL   = "http://localhost:5000/api/v1/account"
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
    ID       = 3

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
    response = requests.post(URL+"/auth?api_key="+TOKEN,json=input_account)

    print(response.json())
    print("@mock_auth_account [end]")

def mock_update_account():
    print("@mock_update_account")

    naccount = {
        "spec" : "somethign new"
    };
    response = requests.put(URL+'/'+str(ID)+"?api_key="+TOKEN,json=naccount)
    
    print(response.json())
    print("@mock_update_account [end]")

def mock_get_all_accounts():
    print("@mock_get_all_accounts")

    response = requests.get(URL+"/?api_key="+TOKEN)

    print(response.json())
    print("@mock_get_all_accounts [end]")

def mock_create_config():
    print("@mock_create_config")

    global IDC

    input_config = {
        "name" : "somenameconfig",
        "states" : [],
        "intents": []
    };
    response = requests.post(URL+"/"+str(ID)+"/config?api_key="+TOKEN,json=input_config)
    IDC      = response.json()["id"]

    print(response.json())
    print("@mock_create_config [end]")


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

    response = requests.put(URL+"/config/"+str(IDC)+"?api_key="+TOKEN,json=input_config)

    print(response.json())
    print("mock_update_config [end]")

def mock_config_delete():
    print("@mock_config_delete")

    global IDC
    response = requests.delete(URL+"/config/"+str(IDC)+"?api_key="+TOKEN)

    print(response.content)
    print("@mock_config_delete [end]")

def mock_get_all_configs():
    print("@mock_get_all_configs")

    global ID
    response = requests.get(URL+"/"+str(ID)+"/config?api_key="+TOKEN)

    print(response.json())
    print("@mock_get_all_configs [end]")

def mock_account_delete():
    print("@mock_account_delete")

    global ID
    response = requests.delete(URL+"/"+str(ID)+"?api_key="+TOKEN)

    print(response.content)
    print("@mock_account_delete [end]")

if __name__ == "__main__":
    mock_create_account()
    """
    print("\n")
    mock_get_all_accounts()
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
    print("\n")
    mock_account_delete()
    print("\n")
    """
