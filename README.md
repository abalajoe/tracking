# Tracking
Supply Chain Track and Trace System

# Demo
I have deployed a demo application at http://159.223.29.248/

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
