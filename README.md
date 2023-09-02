# CleverBank
Project for trainee position:
 Banking application working through servlets, with the ability to deposit, withdraw, transfer funds, create statements
 
- Java 17
- Gradle
- PostgreSQL
- JDBC
- Lombok
- Servlets
- Docker (Half-implemented)


<h3> Features </h3>

- Deposit funds into an account
- Withdraw funds from account
- Transfer funds between two accounts
- Creation of statements of certain periods ("Whole","Year","Month")
- Creation of statements of total balance of certain periods ("Whole","Year","Month")
- All transactions and operation are saved in from of cheques in txt and pdf formats.
- Interest calculation function - every 30 seconds, the interest is calculated depending on the value of "InterestRate" in the ```src/main/resources/config.yaml``` file.
  It is calculated on every last day of the month

CRUD operations for entities: Banks, Users, Accounts, Transactions.
  
<h1>
<b> Set-up </b>
</h1>

- All DB query commands for schemas are saved in ```postgres/ddl```
- Configure DB in ``` src/main/resources/database.properties ```
- Build .war with Gradle
- Set up Tomcat server, using builded .war
- Operate the app with Postman or other API platform.

<h1> <b> Usage --- Postman commands </b> </h1>




