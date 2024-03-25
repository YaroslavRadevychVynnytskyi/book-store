# ONLINE BOOK STORE API
![](https://thumbs.wbm.im/pw/medium/f93cd1aee933a9c37d0ee33931f32939.avif)
___
## Introduction
Welcome to Online Book Store API . This project is a Java-written RESTful interface for 
generic online book store management. It provides you with robust and useful basic endpoints 
necessary for creating full-scale online bookshop. With this API, you can perform various 
operations such as adding new books, updating book details, retrieving book information, 
adding books to shopping cart and posting orders. This API also supports user authentication 
and authorization, allowing you to restrict access to certain endpoints based on user roles.
___
## Technologies and libraries used in the project
1. ### Java
   * **Java** is hugely popular programming language that is used widely for building enterprise-level
     applications. It gained recognition for its robustness, scalability and security.
2. ### Spring Boot
   * **Spring Boot** is a wide-spread framework for building Java applications. It provides a set of tools
     and libraries that simplify the development process and make it easier to build reliable and
     flexible software. Spring Boot is used to build **Online Book Store API** supplying features
     such as Dependency Injection, security, data access and web development.
3. ### MySQL
   * **MySQL** is a solid open-source relational database management system that is widely used for
     web applications. This application uses it for storing data about users, books, and other related
     information.
4. ### Maven
   * **Maven** is a build automation tool used for Java projects. It serves as a standard way to manage
     project dependencies, project builder, and test runner.
5. ### Jackson
   * **Jackson** is a library for processing JSON in Java. It provides features for parsing JSON, 
     generating JSON, and mapping JSON to Java objects.
6. ### Mapstruct
   * **Mapstruct** is a code generator library that automates the mapping between Java objects. It
     provides a way to define the mappings between objects using annotations and generates
     the mapping code automatically during the build phase of project. In this project Mapstruct 
     is used for mapping between domain models and data transfer objects.
7. ### Liquibase
   * **Liquibase** is a database migration tool that allows to manage database schema changes. 
     Same as **Mapstruct** it does its work during the building stage of the application.  
8. ### Swagger
    * **Swagger** is an open-source tool for creating API documentation.
___
## API Architecture
The Online Book Store API is designed to be scalable, maintainable and flexible RESTful API, so it 
is built according to N-tier Architecture. The API is divided into a number of layers, each with specific
responsibility:
 * **Controller layer** - This layer is responsible for handling incoming ***HTTP*** request and returning
   responses.
 * **Service layer** - This layer is responsible for implementing the business logic of the API.
 * **Repository layer** - This layer is in charge of interacting with the database.
   It uses ***Spring Data JPA*** to provide a simple and flexible way to access the database.
 * **Model layer** - This layer contains domain object models used by the API.
___
## API Features Overview
1. **Authentication management endpoints:**
    * Available for everybody:\
```POST: /api/auth/registration``` - registers a new user.\
```POST: /api/auth/login``` - sign in for existing user.
2. **Book management management endpoints:**
    * Administrator available:\
```POST:   /api/books```   - creates a new book.\
```PUT:    /api/books/{id}``` - updates an existing book.\
```DELETE: /api/books/{id}``` - deletes a book.
   * User available:\
```GET: /api/books```      - retrieves all books.\
```GET: /api/books/{id}``` - retrieves certain book.\
```GET: /api/books/search``` - search a book with parameters.
3. **Category management endpoints:**
    * Administrator available:\
```POST:   /api/categories``` - creates a new category.\
```PUT:    /api/categories/{id}``` - updates an existing category.\
```DELETE: /api/categories/{id}``` - deletes a category.
    * User available:\
```GET: /api/categories``` - retrieves all categories.\
```GET: /api/categories/{id}``` - retrieves certain category.\
```GET: /api/categories/{id}/books``` - provides books by category id.
4. **Shopping cart management endpoints:**
    * User available:\
```POST:   /api/cart``` - puts book into shopping cart.\
```GET:    /api/cat/``` - retrieves shopping cart.\
```PUT:    /api/cart/cart-items/{cartItemId}``` - updates quantity of books.\
```DELETE: /api/cart/cart-items/{cartItemId}``` - deletes book from cart.
5. **Order management endpoints:**
    * Administrator available:\
```PATCH:  /api/orders/{id}``` - updates status of the order.
    * User available:\
```POST: /api/orders``` - places an order.\
```GET:  /api/orders``` - retrieves user's order history.\
```GET:  /api/orders/{orderId}/items``` - provides a list of items containing in order.\
```GET:  /api/orders/{orderId}/items/{itemId}``` - provides order item relying on its id.

Here is public [Postman](https://www.postman.com/yaroslavradevych/workspace/book-store-api) 
collection with full URLs. You can use this for testing. 
___
# Setting Up the Application
This chapter contains general instructions for setting up the ***Online Book Store API***.

## Requirements
Before you can set up the application, you will need to have the following software installed
on your machine:
1. Java Development Kit (JDK) version 17.0.10 or higher
2. Apache Maven version 3.8.7 or higher
3. MySQL Server 8 or MariaDB 11.2.3 if you are a Linux user
4. Docker (optionally)

Here is step-by-step instruction how to run the code if you don't have docker:
1. Clone this repository to your computer ```git clone git@github.com:YaroslavRadevychVynnytskyi/book-store.git```
2. Move to **book-store** directory and delete all tests and contextLoads()
3. Configure database connection in application.properties file
4. Build the project using ```mvn clean package```
5. Run the project by ```java -jar book-store-0.0.1-SNAPSHOT.jar``` command

Here is step-by-step instruction how to run the code if you have docker:
1. Clone this repository to your computer
2. Move to **book-store** directory
3. Create file with name **.env** and configure it by entering your port values. You can see
   structure of this file in .env.template 
4. Turn on your Docker
5. Execute command ```docker-compose build``` and ```docker-compose up``` to run 
   the application
6. To stop the application and remove containers you can use ```docker-compose down``` or manually
   turn down containers by typing ```docker stop <container_id>```
___
# API work display
In this section you will be shown couple basic use cases of the application
### 1. Registration use case:
  ![](https://github.com/YaroslavRadevychVynnytskyi/book-store/blob/main/readme-gifs/registration.gif)
### 2. Login use case:
  ![](https://github.com/YaroslavRadevychVynnytskyi/book-store/blob/main/readme-gifs/login.gif)
### 3. Adding a new book:
  ![](https://github.com/YaroslavRadevychVynnytskyi/book-store/blob/main/readme-gifs/add-new-book.gif)
### 4. Putting book into cart:
  ![](https://github.com/YaroslavRadevychVynnytskyi/book-store/blob/main/readme-gifs/put-book-into-cart.gif)
### 5. Placing an order:
  ![](https://github.com/YaroslavRadevychVynnytskyi/book-store/blob/main/readme-gifs/place-order.gif)
___
# Challenges
Since this is my first Java Spring Boot application, obviously there were many moments when I struggled.
Every day I faced new challenges that required me to learn and adapt quickly. One of the biggest challenges 
I faced was Docker. Not the Docker itself, but the fact that it was almost impossible to use it normally
on my Windows-driven laptop. Unfortunately, it was also impossible to install another OS on that laptop. To overcome
this challenge, I had to switch to another computer and install Linux on it. This was not an easy decision as 
it required me to set up a new environment and transfer all my files to the new computer. 
However, it was necessary to ensure that I could use Docker and complete the project successfully.
