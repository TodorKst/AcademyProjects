# 🏥 Medical Record System

A Spring Boot application designed to manage and track patients’ medical histories, doctor profiles, diagnoses, visits, and sick leaves.
This system features robust role-based access control, statistical reporting, Swagger UI with JWT authorization, and full CRUD capabilities.

---

## 🔍 Features

#### 🧑‍⚕️ Doctors
- Unique identification
- Multiple specialties
- Can be flagged as a **General Practitioner (GP)**
- View their own patients’ records
- Create diagnoses and issue treatments or sick leaves

#### 👩‍⚕️ Patients
- Identified by name and personal ID
- Must be assigned to a GP
- Insurance validation for last 6 months
- Can visit any doctor
- Can **only view** their own records

#### 📋 Medical Visits
- Records of diagnoses
- Prescribed treatments
- Associated with a doctor and patient
- Can lead to a **Sick Leave**

#### 🧾 Diagnoses
- Fully normalized entity
- Connected to multiple visits

#### 💤 Sick Leaves
- Linked to a visit
- Validated for non-overlap and logical dates

---

## 🛠 CRUD Operations Implemented

| Entity         | Create | Read | Update | Delete |
|----------------|--------|------|--------|--------|
| Doctors        | ✅      | ✅    | ✅      | ✅      |
| Patients       | ✅      | ✅    | ✅      | ✅      |
| Diagnoses      | ✅      | ✅    | ✅      | ✅      |
| Medical Visits | ✅      | ✅    | ✅      | ✅      |
| Sick Leaves    | ✅      | ✅    | ✅      | ✅      |
| Specialties    | ✅      | ✅    | ✅      | ✅      |
| Admins         | ✅      | ✅    | ✅      | ✅      |

---

## 📈 Reports & Statistics

1. 🧬 **Patients with a specific diagnosis**
2. 📊 **Most frequent diagnoses**
3. 👨‍⚕️ **Patients under a specific GP**
4. 🧮 **Patient count per GP**
5. 📋 **Visit count per doctor**
6. 🗂 **Visit history per patient**
7. 🗓 **Visit history within a period (all doctors or one doctor)**
8. 📅 **Month with most sick leave records**
9. 🏆 **Doctors who issued the most sick leaves**

---

## 🔐 User Roles and Permissions

| Role        | Permissions                                                |
|-------------|------------------------------------------------------------|
| **Patient** | View their own medical history                             |
| **Doctor**  | View all data, modify only their patients’ medical records |
| **Admin**   | Full access to all system data                             |

Implemented using Spring Security + JWT (stateless session).

---

## ⚙️ Technical Highlights

### 🧱 Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- JPA/Hibernate
- MySQL
- Lombok
- Maven/Gradle compatible
- ModelMapper & DTO layer

---

### 🔒 Security
- Passwords encoded using `BCrypt`
- Full **JWT-based authentication** with role-based authorization
- Custom `UserDetailsService` integrated with Spring Security
- Role-specific access using `@PreAuthorize` and controller-level restrictions

---

### 🧪 Testing
- Unit tests and integration tests planned/implemented where needed
- `ValidationHelper` used for centralized domain validation logic

---

### 📄 Swagger (OpenAPI)
- Integrated using **springdoc-openapi**
- JWT token input enabled for **authorized API testing**
- Clear separation by endpoints (Patient, Doctor, Admin, etc.)

---

### 🗃 Database Design
- Fully normalized relational schema
- Proper use of `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@DiscriminatorValue` for inheritance
- All timestamp fields default to current time where appropriate

---

## 🧠 Design Decisions
- Used inheritance and polymorphism for `User` → `Doctor`, `Patient`, `Admin`
- DTO-layer abstraction to protect domain models
- Fully validated entities using `javax.validation`
- Centralized validation logic in `ValidationHelper`
