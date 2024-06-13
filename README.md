[![forthebadge](https://forthebadge.com/images/badges/cc-0.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-javascript.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/uses-css.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)


# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

> git clone https://github.com/TBERT31/P5-yoga-app

Go inside folder:

> cd yoga-app

### Launch Frontend

Go inside folder:

> cd front

Install dependencies:

> npm install

Launch Front-end:

> npm run start;

Frontend development server is available, Navigate to `http://localhost:4200/`. 
The application will automatically reload if you change any of the source files.

### Launch Backend

The backend of this project is built with Java version 1.8 and Spring Boot version 2.6.1.

#### Prerequisites

Before starting with the backend setup, ensure you have the following installed:

- **Java JDK 1.8** or later: You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html).
- **Maven**: Maven is used for building and managing dependencies. You can download it from the [official Maven website](https://maven.apache.org/download.cgi).
- **MySQL**: This project uses MySQL as the database. You can download it from the [official MySQL website](https://dev.mysql.com/downloads/installer/).

Navigate to the backend directory :
> cd ../back

Database Setup : 
Import the SQL schema script available at ressources/sql/script.sql into your MySQL instance. This script sets up the initial database schema required for the application. 

Environment Configuration : 

Main directory :
Modify any necessary environment variables or configurations in the application.properties file located in back/src/main/resources.
Personaly I created a database named yoga in MySQL, you can do the same.

Test directory :
Modify any necessary environment variables or configurations in the application-integrationtest.properties file located in back/src/test/resources.
Personaly I created a database named yoga_test in MySQL, you can do the same.

Build and Run : 

> mvn clean install
> mvn spring-boot:run

This will compile the application, run all tests, and start the backend server on http://localhost:8080/.
You will need to recompile the code for changes made in any of the source files to take effect.

## Ressources

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### E2E

Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> front/coverage/lcov-report/index.html

#### Unitary test

Launching test:

> npm run test

for following change:

> npm run test:watch

Generate coverage report :

> npm run test -- --coverage

Report is available here:

> front/coverage/jest/lcov-report/index.html

#### Jacoco code coverage


For launch test with maven :

> mvn clean test

Then generate the code coverage with Jacoco :

> mvn jacoco:report

Report is available here:

> back/target/site/jacoco/index.html
