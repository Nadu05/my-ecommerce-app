
# E-Commerce App

A full-featured e-commerce web application built with Spring Boot, Thymeleaf, and Bootstrap.

## Features

- User registration, login, and profile management
- Product catalog with categories, search, and sorting
- Shopping cart and checkout process
- Order management and order history
- Responsive UI with Bootstrap

## Project Structure

```
e-commerce-app/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       ├── static/
│   │       └── templates/
│   └── test/
└── target/
```

## Getting Started

### Prerequisites

- Java 11
- Maven 3.6+

### Running the Application

From the project root, run:

```sh
./mvnw spring-boot:run
```
or
```sh
mvn spring-boot:run
```

Alternatively, build and run the JAR:

```sh
mvn clean package
java -jar target/ecommerce-app-0.0.1-SNAPSHOT.jar
```

The app will be available at [http://localhost:8080](http://localhost:8080).

### Default Users

- **Admin:**  
  Username: `admin`  
  Password: `admin123`

- **User:**  
  Username: `user`  
  Password: `user123`

## Configuration

- Edit `src/main/resources/application.properties` for database and other settings.
- Sample data is loaded from `src/main/resources/data.sql`.

## Folder Structure

- `src/main/java/com/ecommerce/` - Java source code (controllers, services, models, etc.)
- `src/main/resources/templates/` - Thymeleaf HTML templates
- `src/main/resources/static/` - Static assets (CSS, JS, images)
- `src/main/resources/application.properties` - Application configuration
- `src/main/resources/data.sql` - Initial database data

## License

This project is for educational purposes.