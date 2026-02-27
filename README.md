
# ApexTrack – IT Asset Management System
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Junit](https://img.shields.io/badge/junit-%23E33332?logo=junit5&logoColor=white)
![Railway](https://img.shields.io/badge/Railway-131415?style=for-the-badge&logo=railway&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellijidea&logoColor=white)
[![Java CI with Maven (Apex-Track)](https://github.com/NishantKumar-Astro/Apex-Track/actions/workflows/main.yml/badge.svg)](https://github.com/NishantKumar-Astro/Apex-Track/actions/workflows/main.yml)

ApexTrack is a secure REST API for tracking IT assets (laptops, servers, monitors) assigned to employees. It automatically calculates decommission dates and asset status (ACTIVE/DECOMMISSIONED). 
Built with Spring Boot, 
JWT authentication,
Postman,
PostgreSQL.

## 🚄 Deployment Platform- Railway                                                       
https://apex-track-production.up.railway.app/swagger-ui/index.html#/

## ✨ Features

- **Testing** - Junit and Mockito                                                      
- **Employee Management** – Register employees (users) with secure login (JWT)
- **Asset Tracking** – Add, update, delete, and view assets
- **Automatic Decommission** – Assets are decommissioned 4 years after assignment (configurable)
- **Status Calculation** – Assets are automatically marked ACTIVE or DECOMMISSIONED based on current date
- **Employee‑Asset Relationship** – Each asset can be assigned to an employee
- **Custom Queries** – Filter assets by status (active/decommissioned) or by employee
- **Upgrade Notifications** – Identify assets needing upgrade (within 30 days of decommission)

## 🛠️ Tech Stack

- **Java 21**                                                  
- **Spring Boot 3.5.9** (Web, Security, Data JPA)                                          
- **Spring Security** with JWT (io.jsonwebtoken)                                                  
- **PostgreSQL** (production) / **H2** (development)                                            
- **Lombok** – reduce boilerplate                                                
- **Maven** – build tool                                                  

## 📁 Project Structure

ApexTrack/                                             
├── src/main/java/com/example/ApexTrack/                                                 
│ ├── Security/ – JWT filter and security configuration                                    
│ ├── Controller/ – REST controllers (Employ, Asset)                                          
│ ├── Model/ – JPA entities (Employ, Asset, UserPrincipal)                                    
│ ├── repository/ – Spring Data JPA repositories                                                
│ └── Service/ – Business logic (EmployService, AssetService, etc.)                                                
├── src/main/resources/                                                          
│ └── application.properties                                                                
└── pom.xml                                                                


## 🚀 Getting Started

### Prerequisites

- Java 21                                    
- Maven                                
- PostgreSQL (optional, H2 works out-of-the-box)                                  

### Installation

1. **Clone the repository**                                        
      ```bash                                          
      git clone https://github.com/yourusername/ApexTrack.git                                        
      cd ApexTrack                                              

2. Configure database (skip for H2)
```
   Edit src/main/resources/application.properties:                    
   spring.datasource.url=jdbc:postgresql://localhost:5432/apextrack_db                  
   spring.datasource.username=yourusername                                    
   spring.datasource.password=yourpassword                          
   spring.jpa.hibernate.ddl-auto=update                                         
```
3.Build and run
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
API will be available at http://localhost:8080.

📋 API Endpoints
Employee (User) Endpoints                                    
```
Method	Endpoint	Description	Auth Required                                                
POST	/api/employ/register	Register new employee	No                                        
POST	/api/employ/login	Login & receive JWT	No                                        
GET	/api/employ	Get all employees	Yes                                      
GET	/api/employ/{id}	Get employee by ID	Yes                                            
DELETE	/api/employ/{id}	Delete employee (and assets)	Yes                                
```

Asset Endpoints (JWT required)                                            
```
Method	Endpoint	Description                                        
GET	/api/assets	Get all assets                                            
GET	/api/assets/{id}	Get asset by ID                                            
GET	/api/assets/employ_id/{id}	Get assets assigned to an employee                                              
GET	/api/assets/active	Get all active assets                                                    
GET	/api/assets/decommissioned	Get all decommissioned assets                                                
POST	/api/assets	Add a new asset (auto-calculates dates)                                      
PUT	/api/assets/{id}	Update asset details                                          
DELETE	/api/assets/{id}	Delete asset                                        
```
🔍 Sample Queries
Register an employee                              

```json                                        
POST /api/employ/register                                                  
{                                                  
  "username": "alice",                                      
  "email": "alice@company.com",                                              
  "password": "secret",                                          
  "department": "Engineering"                                        
}                                          
```
Add an asset (auto‑assigns current date and decommission date = today + 4 years)                                        

```json                                                        
POST /api/assets                                                  
Authorization: Bearer <your-token>                                                        
{                                                              
  "serial_no": "LAP12345",                                        
  "type": "Laptop",                                                    
  "assigned_date": "2025-03-01",                                            
  "employ": { "id": 1 }                                                  
}                                                
```
Get assets needing upgrade (within 30 days of decommission)                                            


```text                                                
GET /api/assets/upgrade-needed                                        
🧮 Business Logic                                                    
Decommission date = assignment date + 4 years (configurable in AssetService)                                          

Status:                                

ACTIVE – if current date ≤ decommission date                                                    

DECOMMISSIONED – if current date > decommission date                                                  

Upgrade needed – assets with decommission date within the next 30 days                                            
```

🗂️ Database Schema  
Database schematics(coming soon)                                       

🔮 Future Enhancements                                      
Add email alerts for upcoming decommission dates                                                  

Implement asset depreciation tracking                             

Create a dashboard frontend                                                                           

Add role-based access (admin vs. employee)                                    

🤝 Contributing                                             
Open to suggestions and improvements – feel free to fork and submit PRs.                                          

📄 License                                                            
This project is for demonstration purposes.                                          

📬 Contact                                                   
Nishatn Kumar– infinityseeker@gamil.com                                                
GitHub: NishantKumar-Astro                                          
