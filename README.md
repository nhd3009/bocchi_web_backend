# 🛒 E-Commerce Backend API (Frontend in developement)

This is the backend service for a simple E-Commerce application, built with **Spring Boot**, **Spring Security**, **JWT**, and **MySQL**.  
It uses Custom API Response to provide RESTful APIs for user authentication, product management, order processing, review & rating, etc.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot 3
- Spring Security + JWT + OAuth2
- MySQL
- Maven

---

## 📦 Features

- ✅ Custom API Response
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
You can import ***bocchiweb_db.sql*** to create database or you can create your own database
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
- App runs on: http://localhost:8080
- Account for Admin: admin@gmail.com
- Password for Admin: admin
- Account for User: user@gmail.com
- Password for User: user

## 📖 API Documentation
Swagger UI is available at:
👉 http://localhost:8080/swagger-ui/index.html

## Screenshots:
![image](https://github.com/user-attachments/assets/2e0f7270-682b-4cdf-becc-6d257f8188b6)
![image](https://github.com/user-attachments/assets/ffa274e4-6529-477f-88e4-3435a4c1fd54)
![image](https://github.com/user-attachments/assets/383135b1-32cd-461d-b52b-19241c4f86fe)

## 👨‍💻 Author
- Cupcake
- GitHub: @nhd3009

## 🐳 Run with docker
1. Build and run with Docker Compose
```
docker-compose up -d
```
- The backend app will be available at: http://localhost:8081
- MySQL will be available at: localhost:3307 (user: bocchi, password: bocchi123, database: bocchiweb)
- The uploads folder will be mounted for file storage.

2. Stop all containers
```
docker-compose down
```

3. Rebuild the image when u want to change the code
```
docker-compose up -d --build
```

4. Access Swagger UI
```
http://localhost:8081/swagger-ui/index.html
```

5. Notes

- If port 8081 or 3307 is already in use on your machine, edit the ports section in docker-compose.yml.