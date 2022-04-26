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
    │   └───resources
    └───test
        └───java      # Java test files
            └───com
                └───revature
                    ├───account       #
                    ├───jdbc          #
                    ├───transaction   #
                    └───user          #
```

## Technologies

- Java (Maven Project)
- PostgreSQL

### Maven Dependencies

- Javalin
- Log4j
- JUnit
- Mockito

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