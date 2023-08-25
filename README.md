# rewards-calculator

This project is a Rewards Calculator Springboot based REST services. This service use in-memory H2 embedded database for storing data related to transactions. It has two endpoints, one is to get rewards in last three months and another one is to add a transaction.  

Postman results are included in results folder for both success and negative scenarios.


## API Details
### GET REWARDS
This endpoint calculates rewards for a particular user in the last three months. Transactions for last three months are fetched from H2 in-memory database. Response contains reward points for each month and total reward points for past three months. This is a GET endpoint which accepts userId as path parameter for which reward points needs to be fetched.

This service has data.sql with some pre-loaded test data which is loaded into in-memory database on running the service. Pre-loaded data contains transactions for two userIds 1 and 2. 

Endpoint format: GET /users/{userId}/rewards

Sample Output:
```json
{
    "month1points": 328,
    "month2points": 132,
    "month3points": 276,
    "totalPoints": 736
}
```

### ADD TRANSACTION
This is a POST endpoint that can add a transaction details into in-memory H2 database for a given particular userId. Request body contains two fields transaction amount and date which are mandatory and date should be in valid format MM-dd-yyyy. Response is simple string with message "Success"

Endpoint format: POST /users/{userId}/transactions

Sample Input

```json
{
    "amount" : "100",
    "date": "07-15-2023"    
}
```

Sample Output
```json
Success
```

