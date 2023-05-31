# Tracking
Supply Chain Track and Trace System - Java Springboot Backend Service

# Demo
I have deployed a demo application at http://159.223.29.248/

The credentials are under authentication section below.

# Access Control
ADMIN ROLE - Create supply, edit supply, update supply event, query individual supply events\
USER ROLE - View supply, query individual supply events 

# Authentication
The init script creates 3 users by default, 1 admin user and 2 customers as below:

admin@gmail.com - admin@123!\
johndoe@gmail.com - user@123!\
marymoss@gmail.com - user@123!

# Swagger
The swagger API is located at http://159.223.29.248:8080/swagger-ui.html

# JSON Schema
The postman collection can be located at the root of the project (tracking collection.postman_collection.json)

# Docker files
The Docker file for the engine can be located at the root folder. The docker-compose file for the whole project can also be located at the root of the project (docker-compose.yml).

# Unit tests
The unit tests for the supply chain resource can be located at iota/src/test

# How to run
1. Copy the sql folder into your root folder. This contains the init.sql script.
2. Copy the docker-compose.yml file into your root folder.
3. install docker and docker-compose.
4. Change the API_URL environment variable under iotatrackingweb to your server url.
5. Run docker-compose up.
6. Access the web application on port 80.
