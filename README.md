# Application for creating Check of products in console and CSV files
This repository contains 5 branches: feature/entry-core, feature/entry-file, feature/entry-database, feature/entry-rest, and feature/custom. Each of these branches contains different application functionality for creating checks with a different stack of technologies. This project was created for educational purposes to understand the workings of such technologies as Servlets API, JDBC, PostgreSQL, and so on.
## Table of Contents
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Example of usage](#example-of-usage)
- [Note](#note)


## Getting Started
### Prerequisites
Make sure you have the following installed:
- Java (version 21 or higher)
- PostgreSQL

## Installation

### Database for Linux Ubuntu

1. You need to install PostgreSQL on your Linux Ubuntu. You can read how to do this [here](https://www.digitalocean.com/community/tutorials/how-to-install-postgresql-on-ubuntu-20-04-quickstart).
2. Execute the following commands:
```bash
sudo -i -u postgres
 ```

```bash
createdb check;
 ```
```bash
psql
 ```
```bash
CREATE USER postgres WITH PASSWORD "postgres";
GRANT ALL PRIVILEGES ON DATABASE check TO postgres;
\q
 ```
or if user 'postgres' already exists: 
```bash
ALTER USER postgres WITH PASSWORD 'postgres';
 ```
3. Run the initialization scripts that are located in ./src/main/resources/data.sql

4. Clone the repository:
```bash
git clone https://github.com/vanzoneway/CheckClevertec.git
 ```
5. Run src/main/resources/data.sql script from your database to init data.
6. Compile the project and run it on ApacheTomcat:
```bash
./gradlew deployApp
 ```

or you can

```bash 
./gradlew war
```
Warning!!! It can be some issues if you run gradle.build through IntellijIdea. You have remove war { ... } from build.gradle
- Now uou have to move your created war archive into apache-tomcat-9.0.91/webapps
Next step: 
```bash
./gradlew startTomcat
```

Note: You should execute this command from the CheckClevertec directory.

## Example of usage

```http request
POST http://localhost:8080/check

{
"products": [
{
"id": 1,
"quantity": 5
},
{
"id": 2,
"quantity": 5
}
],
"discountCard": 1234,
"balanceDebitCard": 100
}
```
- returns CSV file with result
  ![img](src/resources/readme_images/example-of-usage-1.png)

```http request
GET http://localhost:8080/products?id=5
```
- returns product from database with id in parameter
```http request
PUT http://localhost:8080/products?id=3

{
"description": "Chocolate Ritter sport 100g.",
"price": 3.25,
"quantity": 5,
"isWholesale ": true
}

```
- update product information in database by id

```http request
DELETE http://localhost:8080/products?id=1
```
- delete product from database by id

```http request
GET http://localhost:8080/discountcards?id=1
```
- returns information about discount card by id

```http request
POST http://localhost:8080/discountcards

{
"discountCard": 5265,
"discountAmount": 2
}
```
- adds discount card in database

```http request
PUT http://localhost:8080/discountcards?id=1

{
"discountCard": 6786,
"discountAmount": 3
}
```
- update information about discount card in database. It cannot be to or more discount cards with similar card number
```http request
DELETE http://localhost:8080/discountcards?id=1
```
- delete discount card from database by id



## Note
You can read all the information about the Task at src/main/resources/task (see Task 3 and all its appendices). 