# 🛒 E-Commerce Backend API

This is the backend service for a simple E-Commerce application, built with **Spring Boot**, **Spring Security**, **JWT**, and **MySQL**.  
It provides RESTful APIs for user authentication, product management, order processing, review & rating, etc.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot 3
- Spring Security + JWT
- MySQL
- Lombok
- Maven

---

## 📦 Features

- ✅ User Registration / Login / Refresh Token
- ✅ Role-based Authorization (Admin / User)
- ✅ Product Management (CRUD)
- ✅ Category Management
- ✅ Cart & Checkout
- ✅ Order Processing
- ✅ Review & Rating
- ✅ Pagination & Filtering

---
## ⚙️ Getting Started

### 1. Clone this repository

```
git clone https://github.com/your-username/ecommerce-backend.git
cd ecommerce-backend
```
### 2. Configure the database
Update application.properties
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_scheme
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```
### 3. Build and Run
```
./mvnw clean install
./mvnw spring-boot:run
```
App runs on: http://localhost:8080

## 📖 API Documentation
Swagger UI is available at:
👉 http://localhost:8080/swagger-ui/index.html

## 👨‍💻 Author
- Cupcake
- GitHub: @nhd3009


