#  MediCare - Online Pharmacy & Doctor Management System

A full-stack web application that connects **Patients**, **Doctors**, **Pharmacies**, and **Administrators** on a unified digital healthcare platform.

---

##  Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Project Structure](#-project-structure)
- [How to Run](#-how-to-run)
- [Test Credentials](#-test-credentials)
- [Screenshots](#-screenshots)
- [Future Scope](#-future-scope)
- [Developer](#-developer)

---

##  Features

###  Patient
- Register & Login with BCrypt encrypted passwords
- Browse doctors dynamically loaded from database
- Book appointments with date, time, and reason
- Real-time text chat with doctor after booking
- View e-prescriptions with diagnosis and medicine notes
- See prescription status (Active / Fulfilled)
- Browse medicine catalog with Rx/OTC labels
- Place orders with quantity and delivery address
- Prescription verification — Rx medicines require valid prescription
- One-time prescription fulfillment — becomes Fulfilled after order
- Demo payment integration
- Track order status (Pending → Confirmed → Shipped → Delivered)
- Quick-fill address buttons for easy testing

###  Doctor
- Dashboard with appointment statistics
- View all booked appointments with patient details
- Real-time chat with patients who have booked appointments
- Write e-prescriptions (with or without appointment)
- Auto-complete appointment when prescription issued
- View complete prescription history

###  Pharmacy
- Dashboard with pending orders and stock stats
- Manage medicine inventory (add, update stock)
- View incoming orders with delivery addresses
- Process orders: Confirm → Ship → Deliver
- Stock level tracking

###  Admin
- System overview dashboard
- View all registered patients, doctors, pharmacies

---

##  Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Java 21, Spring Boot 3.2 |
| **Architecture** | 3-Layer (Controller → Service → Repository) |
| **Database** | MySQL 8.0 (persistent storage) |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | BCrypt Password Encryption |
| **Frontend** | HTML5, CSS3, JavaScript (Vanilla) |
| **APIs** | RESTful (18+ endpoints) |
| **Build Tool** | Maven |
| **IDE** | IntelliJ IDEA |

---

##  Architecture

FRONTEND (HTML5 + CSS3 + JavaScript - 18 Pages)

│

▼

CONTROLLER LAYER (7 Controllers)

│

▼

SERVICE LAYER (5 Services)

│

▼

REPOSITORY LAYER (6 Repositories)

│

▼

MySQL DATABASE (7 Tables)


---

##  Database Schema

| Table | Description | Key Fields |
|-------|-------------|------------|
| `users` | All system users | id, email, password, role (enum) |
| `appointments` | Patient-doctor bookings | patient_id, doctor_id, date, status |
| `prescriptions` | Digital prescriptions | doctor_id, patient_id, diagnosis, notes, status |
| `medicines` | Pharmacy inventory | name, price, stock_quantity, requires_prescription |
| `orders` | Medicine orders | patient_id, medicine_id, quantity, delivery_address, status |
| `messages` | Chat conversations | sender_id, receiver_id, content, timestamp |

---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |

### Doctors
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/doctors` | List all doctors |
| GET | `/api/doctors/patients` | List all patients |

### Appointments
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/appointments` | Book appointment |
| GET | `/api/appointments/patient/{id}` | Patient's appointments |
| GET | `/api/appointments/doctor/{id}` | Doctor's appointments |
| PUT | `/api/appointments/{id}/status` | Update status |

### Prescriptions
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/prescriptions` | Create prescription |
| GET | `/api/prescriptions/patient/{id}` | Patient's prescriptions |
| GET | `/api/prescriptions/doctor/{id}` | Doctor's prescriptions |

### Medicines
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/medicines` | List all medicines |
| GET | `/api/medicines/search?keyword=` | Search medicines |
| POST | `/api/medicines` | Add medicine |
| PUT | `/api/medicines/{id}/stock` | Update stock |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Place order (with Rx verification) |
| GET | `/api/orders/patient/{id}` | Patient's orders |
| GET | `/api/orders/pharmacy/{id}` | Pharmacy orders |
| PUT | `/api/orders/{id}/status` | Update order status |

### Messages
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/messages` | Send message |
| GET | `/api/messages/conversation?user1=&user2=` | Get conversation |

---

## 📁 Project Structure


### File Count

| Layer | Files | Description |
|-------|-------|-------------|
| **Models** | 6 | JPA entities |
| **Repositories** | 6 | Data access layer |
| **Services** | 5 | Business logic layer |
| **Controllers** | 7 | REST API endpoints |
| **Frontend Pages** | 18 | HTML/CSS/JS |
| **Total Java Files** | 24 | Backend |
| **Total Frontend Files** | 19 | Frontend |


---

##  How to Run

### Prerequisites
- Java 21 installed
- MySQL 8.0 installed and running
- IntelliJ IDEA (Community Edition)
- Maven (comes with IntelliJ)

### Steps

1. Create MySQL database: pharmacy_db
2. Update application.properties with your MySQL password
3. Open project in IntelliJ IDEA
4. Run PharmacyApplication.java
5. Open http://localhost:8080/html/index.html

---

## Test Credentials

Patient: patient@pharmacy.com / patient123

Doctor: doctor@pharmacy.com / doctor123

Pharmacy: pharmacy@pharmacy.com / pharmacy123

Admin: admin@pharmacy.com / admin123

---

## Screenshots

(Add 12 screenshots here)

---

## Future Scope

JWT Auth, Video Calls, Real Payments, Email Alerts, Mobile App, AI Chatbot, Cloud Deploy

---

## Developer

Name: Ulfath Vasima,Sharjana Banu

College: Syed Ammal Engineering College

Department: BE.CSE

Batch:01

Year: 2026

© 2026 MediCare. All rights reserved.