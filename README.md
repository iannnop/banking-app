# Banking App

a java banking application with Javalin API

## Installation

Clone the repository
```
git clone https://github.com/iannnop/banking-app.git
```

## File Structure

```
├───.idea     # IntelliJ IDEA project settings
└───src   
    ├───main
    │   ├───java      # Java source files
    │   │   └───com
    │   │       └───revature
    │   │           ├───account       # Account model/dao/controller
    │   │           ├───jdbc          # JDBC ConnectionManager Singleton
    │   │           ├───exception     # Custom exceptions
    │   │           ├───transaction   # Transaction model/dao/controller
    │   │           └───user          # User model/dao/controller
    │   │
    │   └───resources 
    │   
    └───test
        └───java      # Java test files
            └───com
                └───revature
                    ├───account       # AccountTest methods
                    ├───jdbc          # ConnectionManagerTest methods
                    ├───transaction   # TransactionTest methods
                    └───user          # UserTest methods
```

## Technologies

- Java (Maven Project)
- PostgreSQL
- Javalin REST API

---

### Maven Dependencies

- Javalin REST API
- Log4j
- JUnit 4
- Mockito
- Gson (for parsing JSON strings in REST API request body)

## Features

### Customers

- Customers of the bank should be able to register with a username and password
and apply to open an account
- Customers should be able to apply for joint accounts
- Customers should be able to withdraw, deposit, and transfer funds between accounts

### Employees

- Employees of the bank should be able to view all customer information, including:
  - Account Information
  - Account Balances
  - Personal Information
- Employees should be able to approve/deny open applications for accounts

### Bank Administrators

- Bank admins should be able to view and edit all accounts. This includes:
    - Approving/denying accounts
    - Withdrawing, depositing, transferring from all accounts
    - Canceling accounts

---

## Javalin REST API

### UserController Routes:

<details>
<summary>get-user</summary>

Get a user from the database

**Route**: `/users/{username}`

**Method**: GET

**Response Body (JSON)**:

Successful response returns the JSON object of the requested User

```json
{
  "id": 3,
  "role": "EMPLOYEE",
  "username": "npietrowicz6",
  "password": "o5MX1wqd2Z",
  "userCreated": 1651401461733,
  "firstName": "Nolan",
  "lastName": "Pietrowicz",
  "email": "npietrowicz6@slate.com",
  "phone": "913-886-9898",
  "address": "30924 Springs Road",
  "accounts": []
}
```
</details>

<details>
<summary>create-user</summary>

Creates a user account

**Route**: `/users`

**Method**: PUT

**Request Body (JSON)**:
```json
{
  "role": "CUSTOMER",
  "username": "johnsmith",
  "password": "123456",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john@company.com"
}
```

**Response Body (JSON)**:

Successful response returns the JSON object of the created User

```json
{
  "id": 16,
  "role": "CUSTOMER",
  "username": "johnsmith",
  "password": "123456",
  "userCreated": 1651448905535,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john@company.com",
  "phone": null,
  "address": null,
  "accounts": []
}
```
</details>

<details>
<summary>update-user</summary>

Update a user record in the database

**Route**: `/users/{username}`

**Method**: PUT

**Request Body (JSON)**:
```json
{
  "role": "EMPLOYEE",
  "password": "o5MX211353351Z",
  "firstName": "Nolan",
  "lastName": "Pietrowicz",
  "email": "npietrowicz2226@slate.com",
  "phone": "913-886-9898",
  "address": "30924 Springs Road"
}
```

**Response Body (JSON)**:

Successful response returns the JSON object of the updated User

```json
{
  "id": 16,
  "role": "EMPLOYEE",
  "username": "johnsmith",
  "password": "o5MX21351Z",
  "userCreated": 1651448905535,
  "firstName": "Nolan",
  "lastName": "Pietrowicz",
  "email": "npietrowicz2226@slate.com",
  "phone": "913-886-9898",
  "address": "30924 Springs Road",
  "accounts": []
}
```
</details>

<details>
<summary>delete-user</summary>

Delete a user from the database

**Route**: `/users/{username}`

**Method**: DELETE

**Response Body (JSON)**:

Successful response returns the JSON object of the deleted User

```json
{
  "id": 16,
  "role": "EMPLOYEE",
  "username": "johnsmith",
  "password": "o5MX21351Z",
  "userCreated": 1651448905535,
  "firstName": "Nolan",
  "lastName": "Pietrowicz",
  "email": "npietrowicz2226@slate.com",
  "phone": "913-886-9898",
  "address": "30924 Springs Road",
  "accounts": []
}
```
</details>

### AccountController Routes:

<details>
<summary>get-user-accounts</summary>

Get all the accounts owned by the user

**Route**: `/users/{username}/accounts`

**Method**: GET

**Response Body (JSON)**:

Successful response returns an array of JSON object of the accounts owned by the user

```json
[
  {
    "id": 4,
    "accountCreated": 1651449818797,
    "status": "PENDING_APPROVAL",
    "balance": 200.99,
    "description": null,
    "transactions": [
      {
        "id": 8,
        "senderId": 0,
        "receiverId": 4,
        "transactionCreated": 1651449818873,
        "amount": 200.99,
        "type": "DEPOSIT",
        "description": "Starting balance deposit"
      }
    ]
  }
]
```
</details>

<details>
<summary>get-account</summary>

Get an account by account_id

**Route**: `/accounts/{id}`

**Method**: GET

**Response Body (JSON)**:

Successful response returns a JSON object of the Account with the account_id

```json
{
  "id": 4,
  "accountCreated": 1651449818797,
  "status": "PENDING_APPROVAL",
  "balance": 200.99,
  "description": null,
  "transactions": [
    {
      "id": 8,
      "senderId": 0,
      "receiverId": 4,
      "transactionCreated": 1651449818873,
      "amount": 200.99,
      "type": "DEPOSIT",
      "description": "Starting balance deposit"
    }
  ]
}
```
</details>

<details>
<summary>create-account</summary>

Applies for a user bank account

**Route**: `/users/{username}/accounts`

**Method**: PUT

**Request Body (JSON)**:
```json
{
  "startingBalance": 200.99
}
```

**Response Body (JSON)**:

Successful response returns the JSON object of the created Account

```json
{
  "id": 4,
  "accountCreated": 1651449818797,
  "status": "PENDING_APPROVAL",
  "balance": 200.99,
  "description": null,
  "transactions": [
    {
      "id": 8,
      "senderId": 0,
      "receiverId": 4,
      "transactionCreated": 1651449818873,
      "amount": 200.99,
      "type": "DEPOSIT",
      "description": "Starting balance deposit"
    }
  ]
}
```
</details>

<details>
<summary>update-account</summary>

Update the account with the account_id

**Route**: `/accounts/{id}`

**Method**: PUT

**Request Body (JSON)**:
```json
{
  "status": "ACTIVE",
  "balance": 200.99,
  "description": "My new active account!"
}
```

**Response Body (JSON)**:

Successful response returns the JSON object of the updated Account

```json
{
  "id": 4,
  "accountCreated": 1651449818797,
  "status": "ACTIVE",
  "balance": 200.99,
  "description": "My new active account!",
  "transactions": [
    {
      "id": 8,
      "senderId": 0,
      "receiverId": 4,
      "transactionCreated": 1651449818873,
      "amount": 200.99,
      "type": "DEPOSIT",
      "description": "Starting balance deposit"
    }
  ]
}
```
</details>

<details>
<summary>delete-account</summary>

Delete an account from the database

**Route**: `/accounts/{id}`

**Method**: DELETE

**Response Body (JSON)**:

Successful response returns the JSON object of the deleted Account

```json
{
  "id": 4,
  "accountCreated": 1651449818797,
  "status": "ACTIVE",
  "balance": 200.99,
  "description": "My new active account!",
  "transactions": [
    {
      "id": 8,
      "senderId": 0,
      "receiverId": 4,
      "transactionCreated": 1651449818873,
      "amount": 200.99,
      "type": "DEPOSIT",
      "description": "Starting balance deposit"
    }
  ]
}
```
</details>

### TransactionController Routes:

<details>
<summary>get-account-transactions</summary>

Get all the transactions in an account

**Route**: `/accounts/{id}/transactions`

**Method**: GET

**Response Body (JSON)**:

Successful response returns an array of JSON object of the transactions in the account

```json
[
  {
    "id": 8,
    "senderId": 0,
    "receiverId": 4,
    "transactionCreated": 1651449818873,
    "amount": 200.99,
    "type": "DEPOSIT",
    "description": "Starting balance deposit"
  },
  {
    "id": 9,
    "senderId": 0,
    "receiverId": 4,
    "transactionCreated": 1651450688042,
    "amount": 0.01,
    "type": "DEPOSIT",
    "description": "My new transaction description!"
  }
]
```
</details>

<details>
<summary>get-transaction</summary>

Get a transaction by transaction_id

**Route**: `/transactions/{id}`

**Method**: GET

**Response Body (JSON)**:

Successful response returns a JSON object of the Transaction with the transaction_id

```json
{
  "id": 8,
  "senderId": 0,
  "receiverId": 4,
  "transactionCreated": 1651449818873,
  "amount": 200.99,
  "type": "DEPOSIT",
  "description": "Starting balance deposit"
}
```
</details>

<details>
<summary>create-transaction</summary>

Creates a transaction in the account

**Route**: `/accounts/{id}/transactions`

**Method**: PUT

**Request Body (JSON)**:
```json
{
  "senderId": 0,
  "receiverId": 4,
  "amount": 0.01,
  "description": "My deposit made through javalin banking rest api"
}
```

**Response Body (JSON)**:

Successful response returns the JSON object of the created Transaction

```json
{
  "id": 9,
  "senderId": 0,
  "receiverId": 4,
  "transactionCreated": 1651450688042,
  "amount": 0.01,
  "type": "DEPOSIT",
  "description": "My deposit made through javalin banking rest api"
}
```
</details>

<details>
<summary>update-transaction</summary>

Update the transaction with the transaction_id

**Route**: `/transactions/{id}`

**Method**: PUT

**Request Body (JSON)**:
```json
{
  "description": "My new transaction description!"
}
```

**Response Body (JSON)**:

Successful response returns the JSON object of the updated Transaction

```json
{
  "id": 9,
  "senderId": 0,
  "receiverId": 4,
  "transactionCreated": 1651450688042,
  "amount": 0.01,
  "type": "DEPOSIT",
  "description": "My new transaction description!"
}
```
</details>

<details>
<summary>delete-transaction</summary>

Delete a transaction from the database

**Route**: `/transactions/{id}`

**Method**: DELETE

**Response Body (JSON)**:

Successful response returns the JSON object of the deleted Transaction

```json
{
  "id": 9,
  "senderId": 0,
  "receiverId": 4,
  "transactionCreated": 1651450688042,
  "amount": 0.01,
  "type": "DEPOSIT",
  "description": "My new transaction description!"
}
```
</details>
