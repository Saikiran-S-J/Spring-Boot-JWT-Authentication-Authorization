# Spring-Boot-JWT-Authentication-Authorization
This project demonstrates how to implement **Authentication &amp; Authorization** in a Spring Boot application using **Spring Security** and **JWT (JSON Web Token)**.


## 🚀 Features
- User Registration and Login
- JWT Token generation & validation
- Role-based access control (USER, ADMIN)
- Secure REST APIs
- H2 in-memory database

## 🧩 Tech Stack
- Java 17+
- Spring Boot 3+
- Spring Security
- JWT (jjwt)
- MySql Workbench
- Maven

🧪 API Endpoints
Method	Endpoint	Description
POST	/api/auth/register	Register a new user
POST	/api/auth/login	Login and get JWT token
GET	/api/users/me	Access protected endpoint (Authenticated users only)
GET	/api/users/admin	Access Admin endpoint (Admins only)
