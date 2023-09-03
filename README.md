# CleverBank
Project for trainee position:
 Banking application working through servlets, with the ability to deposit, withdraw, transfer funds, create statements
 
- Java 17
- Gradle
- PostgreSQL
- JDBC
- Lombok
- Servlets
- Tomcat
- Postman
- Mockito
- Docker (Half-implemented)


<h3> Features </h3>

- Deposit funds into an account
- Withdraw funds from account
- Transfer funds between two accounts
- Creation of statements of certain periods ("Whole","Year","Month")
- Creation of statements of total balance of certain periods ("Whole","Year","Month")
- All transactions and operation are saved in from of cheques in txt and pdf formats.
- Interest calculation function - every 30 seconds scans all accounts, the interest is calculated depending on the value of "InterestRate" in the ```src/main/resources/config.yaml``` file.
  It is calculated on every last day of the month

CRUD operations for entities: Banks, Users, Accounts.
  
<h1>
<b> Set-up </b>
</h1>

- All DB query commands for schemas are saved in ```postgres/ddl```
- Configure DB in ``` src/main/resources/database.properties ```
- Build .war with Gradle
- Set up Tomcat server, using builded .war
- Operate the app with Postman or other API platform.

<h1> <b> Usage --- Postman commands </b> </h1>
                          <p1> <b> "/users" </b></p1>



| GET            | POST              | PUT               | DEL               |
|----------------|-------------------|-------------------|-------------------|
| "id":"(int)"   | "id":"(int)"      | "id":"(int)"      | "id":"(int)"      |
|                | "name":"(String)" | "name":"(String)" |                   |
| Get user by id | Create user       | Update users name | Delete user by id |

<p1><b>"/AllUsers" </b> </p1>
|       GET      |
|:--------------:|
| Gets all users |

<p1><b> "/banks"</b></p1>

| GET            | POST                   | PUT                    | DEL               |
|----------------|------------------------|------------------------|-------------------|
| "id":"(int)"   | "id":"(int)"           | "id":"(int)"           | "id":"(int)"      |
|                | "bank_name":"(String)" | "bank_name":"(String)" |                   |
| Get bank by id | Create bank            | Update banks name      | Delete bank by id |

<p1><b> "/AllBanks" </b></p1>

|       GET      |
|:--------------:|
| Gets all banks |

<p1><b> "/accounts" </b></p1>

| GET               | POST                                          | PUT                                                 | DEL                  |
|-------------------|-----------------------------------------------|-----------------------------------------------------|----------------------|
| "id":"(int)"      | "id":"(int)"                                  | "id":"(int)"                                        | "id":"(int)"         |
|                   | "balance":"(double)" (OPTIONAL, 0 BY DEFAULT) | "balance":"(double)" (OPTIONAL)                     |                      |
|                   | "users_id":"(int)" (REFERENCES users.id)      | "users_id":"(int)" (OPTIONAL) (REFERENCES users.id) |                      |
|                   | "banks_id":"(int)" (REFERENCES banks.id)      | "banks_id":"(int)" (OPTIONAL) (REFERENCES banks.id) |                      |
| Get account by id | Create account                                | Update accounts information                         | Delete account by id |

<p1><b> "/AllAccounts" </b></p1>

|        GET        |
|:-----------------:|
| Gets all accounts |

<p1><b> "/AddFunds"  OR  "/WithdrawFunds"</b></p1> 

|          GET         |
|:--------------------:|
| "account_id":"(int)" |
| "amount":"(double)"  |

<p1> <b> "/statement"   OR "/statementTransactions"  </b></p1> 

|                    GET                    |
|:-----------------------------------------:|
|            "account_id":"(int)"           |
| "TimePeriod":"Whole" OR "Year" OR "Month" |

<p1> <b> "/transaction" </b> </p1>

|             GET             |                    POST                    |
|:---------------------------:|:------------------------------------------:|
|  "transaction_id":"(uuid)"  | "AccountToId":"(int)"                      |
|                             | "AccountFromId":"(int)"                    |
|                             | "amount":"(double)"                        |
| Gets info about transaction | Transaction operation between two accounts |













