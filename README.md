# Health-monitoring-system-
The system provides separate workflows for Patients and Doctors, with Firebase used for authentication and cloud-based data storage.

# 🏥 Remote Health Monitoring System

A mobile-based **Remote Health Monitoring System** developed using **Android Studio and Java**. The application helps patients record and monitor their health readings, maintain historical records, manage appointments, and interact with doctors through a centralized digital platform.

The system provides separate workflows for **Patients and Doctors**, with Firebase used for authentication and cloud-based data storage.

---

## 📌 Project Overview

The **Remote Health Monitoring System** is an Android application designed to make health monitoring more accessible and organized.

Traditional healthcare monitoring often depends on physical hospital visits and manually maintained health records. This project provides a digital platform where users can register, log in, enter health readings, view their health history, monitor health information through charts, and mana<img width="562" height="1280" alt="photo_21_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/e2e435a9-0c4c-4cea-a887-e9744cc27537" />
<img width="562" height="1280" alt="photo_20_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/bb874c89-dabc-4003-bac3-6c24d39a842a" />
<img width="562" height="1280" alt="photo_19_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/7400a437-0eb4-4b1f-938c-6c2a26f30495" />
<img width="562" height="1280" alt="photo_18_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/a6f67963-226a-4281-a436-8f14b65ec54f" />
<img width="562" height="1280" alt="photo_17_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/22908589-4616-4dac-a444-b81dc4b1e70d" />
<img width="562" height="1280" alt="photo_16_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/cdd70070-f42d-4719-814b-5369be934020" />
<img width="562" height="1280" alt="photo_15_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/2b72c38b-c86f-4f5a-9bc8-1f2b83e2bdf7" />
<img width="562" height="1280" alt="photo_14_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/d703ae6a-c415-409f-9bc3-b74a064ad690" />
<img width="562" height="1280" alt="photo_13_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/0c3e22f7-6a79-4804-b766-3faae3eb1c74" />
ge appointments.

Doctors can access patient information, review health records, manage appointments, and provide feedback/recommendations.

The project is designed around the concept of remote healthcare monitoring and digital health record management.

### Main Users

* 👤 Patient
* 👨‍⚕️ Doctor

The project report identifies the major entities as **Patient, Doctor, HealthRecord, and Feedback**, with patients having multiple health records and doctors reviewing patient information.

---

# 🎯 Objectives

The main objectives of the project are:

* Develop a user-friendly Android healthcare application.
* Provide secure user registration and login.
* Allow patients to record health parameters.
* Store health records digitally.
* Allow users to view historical health data.
* Provide health charts and insights.
* Support doctor and patient workflows.
* Manage doctor appointments.
* Allow doctors to review patient records.
* Improve accessibility to personal health information.
* Reduce dependency on manually maintained health records.

The project report specifically identifies secure authentication, health parameter storage, real-time/current monitoring, historical records, abnormal-value alerts, and data privacy as project objectives.

---

# ✨ Key Features

## 👤 Patient Features

### Authentication

* Patient registration
* Email and password login
* Logout
* Firebase Authentication
* User session checking

### Health Monitoring

* Add health readings
* Store health information
* View previous readings
* Monitor health information
* Health data visualization
* Health insights

### Patient Dashboard

* Health summary
* Health readings
* Historical information
* Navigation to major application modules

### Health Charts

* Display health information graphically
* View health trends based on stored readings

### Profile

* View patient profile
* Manage profile information
* View personal health statistics
* Logout

### Appointment Management

* View appointments
* Create/manage appointments
* View appointment status
* Edit appointments

---

# 👨‍⚕️ Doctor Features

The application also contains a dedicated doctor workflow.

### Doctor Authentication

* Doctor registration
* Doctor login
* Doctor profile
* Logout

### Doctor Dashboard

* View registered patients
* Access patient information
* Manage patient records

### Patient Records

* View patient health information
* Review health readings
* Manage health records

### Appointment Management

* View appointments
* Manage appointment requests
* Edit appointment information
* Update appointment status

### Doctor Profile

* View doctor information
* Manage profile
* Logout

---

# 🧩 Core Modules

The project is organized into the following major modules.

## 1. Authentication Module

Responsible for:

* Patient registration
* Doctor registration
* Login
* Logout
* Firebase Authentication
* Session management
* User validation

Main classes:

```text
LoginActivity.java
RegisterActivity.java
DoctorRegisterActivity.java
MainActivity.java
SplashActivity.java
ValidationUtils.java
```

---

## 2. Patient Module

Responsible for patient-side functionality.

Main classes:

```text
DashboardActivity.java
ProfileActivity.java
ChartsActivity.java
AppointmentActivity.java
HealthReading.java
Reading.java
HealthReadingsAdapter.java
ReadingsAdapter.java
```

---

## 3. Health Monitoring Module

This module handles health readings entered by users.

Health-related information can include parameters such as:

* ❤️ Heart Rate
* 🌡️ Temperature
* 🩸 Blood Pressure
* 🫁 Oxygen Level / SpO₂
* 🩺 Other health readings supported by the application

The project report specifically describes health data such as heart rate, blood pressure, temperature, oxygen level, and sugar level within the system design and implementation.

---

## 4. Charts & Health Insights Module

Responsible for displaying stored health information in a more understandable form.

Main class:

```text
ChartsActivity.java
```

The dashboard also contains logic for calculating health-related insights from stored records.

---

## 5. Doctor Module

Responsible for doctor-side functionality.

Main classes:

```text
DoctorDashboardActivity.java
DoctorAppointmentsActivity.java
DoctorManageRecordsActivity.java
DoctorProfileActivity.java
```

---

## 6. Appointment Module

Responsible for appointment-related operations.

Main classes:

```text
Appointment.java
AppointmentActivity.java
AppointmentsAdapter.java
DoctorAppointmentsActivity.java
EditAppointmentActivity.java
```

---

## 7. Database Module

The application uses **Firebase Cloud Firestore** for cloud-based data storage.

Firebase is used for:

* User records
* Doctor records
* Health readings
* Appointments
* Patient information
* Health record management

The actual application source initializes `FirebaseFirestore` and accesses collections such as `users`, `doctors`, and `health_readings`.

---

# 🛠️ Technology Stack

| Technology              | Purpose                     |
| ----------------------- | --------------------------- |
| Java                    | Main programming language   |
| Android                 | Mobile application platform |
| Android Studio          | Development environment     |
| XML                     | UI layouts and resources    |
| Firebase Authentication | User authentication         |
| Firebase Firestore      | Cloud database              |
| Firebase Analytics      | Application analytics       |
| AndroidX                | Android support libraries   |
| Material Components     | UI components               |
| RecyclerView            | Lists and dynamic records   |
| ConstraintLayout        | UI layout design            |
| View Binding            | Accessing XML views         |
| Gradle Kotlin DSL       | Project/build configuration |
| JUnit                   | Unit testing                |
| Espresso                | Android UI testing          |

The actual Gradle configuration confirms Firebase Authentication, Firestore and Analytics, along with AndroidX AppCompat, ConstraintLayout, Material Components, RecyclerView, Fragment and Activity dependencies.

---

# 💻 Languages Used

## Java

Java is the primary programming language used for the Android application.

The project source uses Java classes for:

* Activities
* Models
* Adapters
* Validation
* Firebase operations
* Business logic

The project report also confirms Android Studio as the development environment and Java as the core programming language.

## XML

XML is used for:

* Android layouts
* UI components
* Colors
* Dimensions
* Themes
* Menus
* Drawable resources

---

# 🔥 Firebase Services

The project uses Firebase as the backend infrastructure.

### Firebase Authentication

Used for:

* Registration
* Login
* User authentication
* Session management
* Logout

### Cloud Firestore

Used for storing application data.

Example collections used by the application include:

```text
users
doctors
health_readings
appointments
```

Firestore is accessed through:

```java
FirebaseFirestore.getInstance();
```

### Firebase Analytics

The project also includes Firebase Analytics as a dependency.

---

# 🏗️ Application Architecture

The project follows a modular Android application structure.

```text
+----------------------------+
|       Android UI           |
|        XML Layouts         |
+-------------+--------------+
              |
              v
+----------------------------+
|       Java Activities      |
|  Patient / Doctor Modules  |
+-------------+--------------+
              |
              v
+----------------------------+
|       Firebase Layer       |
| Authentication + Firestore |
+-------------+--------------+
              |
              v
+----------------------------+
|       Cloud Database       |
|     Patient / Doctor Data  |
+----------------------------+
```

The project report describes the overall system as consisting of a mobile application, backend server/data-processing layer, database, and doctor web portal conceptually.

> **Note:** The uploaded Android source contains the mobile application. A separate doctor web portal is described in the report architecture, but it is not present as a separate web project in the uploaded ZIP.

---

# 📂 Folder Structure

The uploaded project contains a nested Android project. The important source structure is:

```text
HealthMonitoringSystem/
│
├── HealthMonitoringSystem/
│   │
│   ├── app/
│   │   ├── src/
│   │   │   │
│   │   │   ├── main/
│   │   │   │   │
│   │   │   │   ├── java/
│   │   │   │   │   └── com/
│   │   │   │   │       └── harshdi/
│   │   │   │   │           └── healthmonitoringsystem/
│   │   │   │   │               │
│   │   │   │   │               ├── Appointment.java
│   │   │   │   │               ├── AppointmentActivity.java
│   │   │   │   │               ├── AppointmentsAdapter.java
│   │   │   │   │               ├── ChartsActivity.java
│   │   │   │   │               ├── DashboardActivity.java
│   │   │   │   │               ├── DoctorAppointmentsActivity.java
│   │   │   │   │               ├── DoctorDashboardActivity.java
│   │   │   │   │               ├── DoctorManageRecordsActivity.java
│   │   │   │   │               ├── DoctorProfileActivity.java
│   │   │   │   │               ├── DoctorRegisterActivity.java
│   │   │   │   │               ├── EditAppointmentActivity.java
│   │   │   │   │               ├── HealthReading.java
│   │   │   │   │               ├── HealthReadingsAdapter.java
│   │   │   │   │               ├── LoginActivity.java
│   │   │   │   │               ├── MainActivity.java
│   │   │   │   │               ├── Patient.java
│   │   │   │   │               ├── PatientsAdapter.java
│   │   │   │   │               ├── ProfileActivity.java
│   │   │   │   │               ├── Reading.java
│   │   │   │   │               ├── ReadingsAdapter.java
│   │   │   │   │               ├── RegisterActivity.java
│   │   │   │   │               ├── SplashActivity.java
│   │   │   │   │               └── ValidationUtils.java
│   │   │   │   │
│   │   │   │   ├── res/
│   │   │   │   │   ├── drawable/
│   │   │   │   │   ├── layout/
│   │   │   │   │   ├── menu/
│   │   │   │   │   ├── mipmap/
│   │   │   │   │   ├── values/
│   │   │   │   │   └── xml/
│   │   │   │   │
│   │   │   │   └── AndroidManifest.xml
│   │   │   │
│   │   │   ├── test/
│   │   │   └── androidTest/
│   │   │
│   │   ├── build.gradle.kts
│   │   ├── google-services.json
│   │   └── proguard-rules.pro
│   │
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── gradle.properties
│   ├── gradle/
│   ├── gradlew
│   └── gradlew.bat
│
├── logo.svg
└── Blood.svg
```

---

# 📱 Important Android Activities

| Activity                      | Purpose                                            |
| ----------------------------- | -------------------------------------------------- |
| `SplashActivity`              | Application splash screen and authentication check |
| `MainActivity`                | Main application routing                           |
| `LoginActivity`               | User login                                         |
| `RegisterActivity`            | Patient registration                               |
| `DashboardActivity`           | Patient dashboard                                  |
| `ChartsActivity`              | Health charts                                      |
| `ProfileActivity`             | Patient profile                                    |
| `AppointmentActivity`         | Patient appointments                               |
| `DoctorRegisterActivity`      | Doctor registration                                |
| `DoctorDashboardActivity`     | Doctor dashboard                                   |
| `DoctorAppointmentsActivity`  | Doctor appointment management                      |
| `DoctorManageRecordsActivity` | Patient health records                             |
| `DoctorProfileActivity`       | Doctor profile                                     |
| `EditAppointmentActivity`     | Edit appointment information                       |

---

# 📦 Important Model Classes

### `Patient.java`

Represents patient information.

### `HealthReading.java`

Represents health monitoring data.

### `Reading.java`

Represents health reading information used by the application.

### `Appointment.java`

Represents appointment information.

---

# 🔄 Adapter Classes

RecyclerView adapters are used to display dynamic information.

```text
AppointmentsAdapter.java
HealthReadingsAdapter.java
PatientsAdapter.java
ReadingsAdapter.java
```

These adapters connect application data with Android RecyclerView layouts.

---

# 🎨 UI & Resources

The application uses XML-based Android UI resources.

### Layouts

```text
activity_login.xml
activity_register.xml
activity_dashboard.xml
activity_charts.xml
activity_profile.xml
activity_add_reading.xml
activity_doctor_dashboard.xml
activity_doctor_appointments.xml
activity_doctor_manage_records.xml
activity_doctor_profile.xml
activity_doctor_register.xml
activity_edit_appointment.xml
```

### Drawable Resources

Used for:

* Buttons
* Cards
* Icons
* Input fields
* Status backgrounds
* UI shapes

### Values

```text
colors.xml
dimens.xml
strings.xml
themes.xml
```

---

# ⚙️ Installation & Setup

## 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/HealthMonitoringSystem.git
```

Then open the project in Android Studio.

---

## 2. Open Project in Android Studio

Open:

```text
HealthMonitoringSystem/HealthMonitoringSystem/
```

in Android Studio.

Do not open the outer ZIP folder if Android Studio does not automatically detect the Gradle project.

---

## 3. Install Requirements

Recommended development environment:

```text
Android Studio
JDK 8+
Android SDK
Android SDK Platform 34
Gradle
```

The project configuration uses:

```text
compileSdk = 34
minSdk = 24
targetSdk = 34
Java source compatibility = 1.8
```

The project report also specifies Android Studio, JDK 8+, Android SDK, and Firebase/SQLite as its software requirements.

---

# 🔥 Firebase Setup

This application requires Firebase.

### Step 1 — Create Firebase Project

Create a project in Firebase Console.

### Step 2 — Add Android Application

Use the Android package/application ID:

```text
com.harshdi.healthmonitoringsystem
```

### Step 3 — Download Configuration

Download:

```text
google-services.json
```

Place it inside:

```text
app/google-services.json
```

### Step 4 — Enable Firebase Authentication

Enable the authentication method required by the application, including:

```text
Email/Password
```

### Step 5 — Create Firestore Database

Enable Cloud Firestore for the Firebase project.

The Android source uses Firebase Authentication and Cloud Firestore directly.

---

# 🗄️ Firestore Data Structure

The application code uses collections including:

```text
users
doctors
health_readings
```

and appointment-related records.

A conceptual structure is:

```text
Firestore
│
├── users
│   └── userId
│       ├── name
│       ├── email
│       ├── phone
│       └── profile information
│
├── doctors
│   └── doctorId
│       ├── name
│       ├── email
│       └── doctor information
│
├── health_readings
│   └── readingId
│       ├── userId
│       ├── heartRate
│       ├── temperature
│       ├── bloodPressure
│       ├── oxygenLevel
│       └── date
│
└── appointments
    └── appointmentId
        ├── patient
        ├── doctor
        ├── date
        ├── time
        └── status
```

> The exact Firestore schema and security rules should be configured according to your Firebase project and the fields used by your final application build.

---

# ▶️ Run the Application

After Firebase configuration:

1. Open the project in Android Studio.
2. Allow Gradle to sync.
3. Connect an Android device or start an emulator.
4. Enable USB debugging if using a physical device.
5. Click **Run ▶**.
6. Select the Android device.
7. Build and install the application.

The application starts from:

```text
SplashActivity
```

which is configured as the launcher activity in the Android manifest.

---

# 🧪 Testing

The project contains both unit-testing and Android instrumentation-testing folders.

Testing categories described in the project report include:

* Unit Testing
* Integration Testing
* System Testing
* Acceptance Testing

Example test scenarios include:

| Test Case     | Input             | Expected Result  |
| ------------- | ----------------- | ---------------- |
| Login         | Valid credentials | Login successful |
| Login Failure | Invalid password  | Error message    |
| Data Entry    | Valid health data | Data saved       |
| Empty Field   | No input          | Validation error |
| Alert Check   | High value        | Alert displayed  |
| View Report   | Report button     | Data displayed   |

The report records these test cases as passed.

---

# ⚠️ Limitations

The current system has some limitations.

### 1. Manual Data Entry

Health readings are entered by the user rather than automatically collected from medical sensors.

### 2. Internet Dependency

Firebase-based functionality requires internet connectivity.

### 3. No Direct Wearable Integration

The current project does not directly connect to smartwatches or IoT medical sensors.

### 4. Limited Automated Diagnosis

The application stores and displays health information but should not be considered a medical diagnosis system.

### 5. Limited AI/ML

Advanced predictive machine-learning functionality is not implemented in the current application.

### 6. Scalability

The current academic project would require additional backend/security optimization before production-scale healthcare deployment.

The project report itself identifies manual data entry and dependency on internet connectivity as limitations.

---

# 🚀 Future Enhancements

The system can be enhanced with advanced healthcare technologies.

## ⌚ Wearable Device Integration

Integrate:

* Smartwatches
* Fitness bands
* Heart-rate sensors
* SpO₂ sensors
* Temperature sensors
* Blood-pressure devices

This would allow automatic health-data collection.

---

## 🤖 AI & Machine Learning

Future versions can use AI/ML to:

* Analyze historical health data
* Detect unusual patterns
* Predict potential health risks
* Generate personalized health insights
* Provide early warnings

---

## ☁️ Advanced Cloud Integration

Improve cloud architecture with:

* Better data backup
* Scalable storage
* Advanced security rules
* Multi-device synchronization
* Cloud-based analytics

---

## 📞 Telemedicine

Future versions can include:

* Video consultation
* Doctor-patient chat
* Voice consultation
* Prescription management
* Online medical consultation

---

## 📅 Advanced Appointment System

Add:

* Doctor availability
* Appointment slots
* Appointment reminders
* Calendar synchronization
* Automatic notifications

---

## 🚨 Emergency Alert System

Possible future features:

* Emergency contacts
* Automatic emergency alerts
* GPS location sharing
* Critical health notifications

---

## 🌐 Multi-Language Support

Support multiple Indian and international languages to improve accessibility.

---

## 🔐 Advanced Security

Future versions can implement:

* Biometric authentication
* Stronger Firestore security rules
* Encryption
* Role-based access control
* Secure audit logs

The project report specifically proposes wearable/IoT integration, cloud integration, AI/ML, teleconsultation, messaging, appointment booking, GPS emergency alerts, multilingual support, biometric authentication, and data visualization as future enhancements.

---

# 🔒 Security Considerations

Because the application handles health-related information, security is important.

Recommended production practices include:

* Firebase Authentication
* Firestore Security Rules
* Role-based access control
* Secure user sessions
* Input validation
* Minimum necessary data collection
* Avoid storing sensitive credentials in source code
* Secure Firebase configuration
* Regular dependency updates

The current application already uses Firebase Authentication and Firestore, but production deployment should include carefully configured Firestore Security Rules.

---

# 📊 Project Workflow

```text
                START
                  │
                  ▼
             Splash Screen
                  │
                  ▼
        Check Authentication
             /          \
            /            \
       Logged In       New User
          │                │
          ▼                ▼
      Dashboard        Registration
          │                │
          │                ▼
          │              Login
          │                │
          └───────┬────────┘
                  │
                  ▼
        ┌───────────────────┐
        │   User Selection  │
        └─────────┬─────────┘
                  │
          ┌───────┴────────┐
          ▼                ▼
       Patient           Doctor
          │                │
          ▼                ▼
    Health Readings    Patient Records
    Charts             Appointments
    Profile            Doctor Profile
    Appointments
          │                │
          └───────┬────────┘
                  ▼
              Firestore
                  │
                  ▼
                 END
```

---

# 📁 Recommended GitHub Repository Structure

For GitHub, it is recommended to avoid uploading generated build/cache files.

```text
HealthMonitoringSystem/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── .gitignore
├── README.md
└── LICENSE
```

### Do NOT upload unnecessary generated folders:

```text
.gradle/
.idea/
app/build/
build/
```

---

# 📚 References

1. World Health Organization — Digital Health resources
2. Android Developers Documentation
3. Firebase Documentation
4. Pressman, R. — *Software Engineering*
5. Sommerville, I. — *Software Engineering*
6. IEEE Standards Association — Software Requirements Specifications

These references are also listed in the original project report.

### Official Documentation

* Android Developers — https://developer.android.com/
* Firebase — https://firebase.google.com/
* Firebase Authentication — https://firebase.google.com/docs/auth
* Cloud Firestore — https://firebase.google.com/docs/firestore
* Material Design — https://m3.material.io/

---

# 👩‍💻 Project Information

**Project:** Remote Health Monitoring System

**Platform:** Android

**Language:** Java

**IDE:** Android Studio

**Database:** Firebase Cloud Firestore

**Authentication:** Firebase Authentication

**UI:** XML + Material Components

**Architecture:** Modular Android Application

**Application ID:**

```text
com.harshdi.healthmonitoringsystem
```

**Minimum SDK:** 24

**Target SDK:** 34

**Compile SDK:** 34

**Version:** 1.0

---

# 👥 Project Team

### Team Members

* **Dhrangadhariya Kinjal Maheshbhai**
* **Bhalara Harshdi Kamleshbhai**

### Institution

**Atmiya University**
Faculty of Science
Department of Computer Science
Rajkot, Gujarat, India

### Project Guide

**Mr. Rahul Bagda**

The project report identifies the project as a BCA Semester VI project for 2025–26 and lists both team members and the project guide.

---

# 📜 Disclaimer

This application is an **academic software project for health monitoring and record management**. It is not intended to replace professional medical diagnosis, treatment, or emergency medical services.

Health readings and insights should be interpreted by qualified healthcare professionals when medical decisions are required.

---

# ⭐ Conclusion

The **Remote Health Monitoring System** demonstrates how Android mobile technology and cloud services can be combined to create a digital healthcare monitoring platform.

The application provides patient and doctor workflows, authentication, health-record management, charts, appointments, profiles, and Firebase-based data management.

The project provides a foundation that can be extended with wearable devices, IoT, AI/ML, telemedicine, emergency alerts, and advanced healthcare analytics.

---

## ⭐ If you find this project useful

Give the repository a ⭐ on GitHub and feel free to explore, improve, and extend the project.




<img width="562" height="1280" alt="photo_22_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/c0838b67-3f28-47d3-9ec8-bdc31cef353c" /><img width="562" height="1280" alt="photo_12_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/4da11915-ffef-496a-87b2-0717fbc86e9e" />
<img width="562" height="1280" alt="photo_11_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/0753a559-8f85-4799-aebe-0bfb99e07db9" />
<img width="562" height="1280" alt="photo_10_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/199cbdeb-34b6-421e-b1e9-2b77b6b07c38" />
<img width="562" height="1280" alt="photo_9_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/84255927-b267-402c-b694-b88bf08b76ac" />
<img width="562" height="1280" alt="photo_8_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/5e8d438b-f1db-4c7b-997e-4012ba822718" />
<img width="562" height="1280" alt="photo_7_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/f32a1500-e573-4e33-9d18-a35989b8d0d5" />
<img width="562" height="1280" alt="photo_6_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/378ecdb7-6f06-4831-bff1-112692d83191" />
<img width="562" height="1280" alt="photo_5_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/2ae7dbce-de7e-4f8e-b41f-d37eedabb62e" />
<img width="562" height="1280" alt="photo_4_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/e760ead9-d876-4d80-b60d-a33e39dce74d" />
<img width="562" height="1280" alt="photo_3_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/086ec3f8-ebad-4b2f-b696-ed40b38f865b" />
<img width="562" height="1280" alt="photo_2_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/671929ad-0093-466d-b410-8f0501dbd5d5" />
<img width="562" height="1280" alt="photo_1_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/a01450c6-c05b-481d-9ca6-a7e52ba3e84f" />
<img width="562" height="1280" alt="photo_34_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/b50a4702-3d12-4d01-a6cd-68495347cdc6" />
<img width="562" height="1280" alt="photo_33_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/881ca04e-b372-4bde-8dd3-22b5ceb8654e" />
<img width="562" height="1280" alt="photo_32_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/3065e446-d36b-4ed3-b164-48096c25bf29" />
<img width="562" height="1280" alt="photo_31_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/a3263d39-f010-407a-b7e5-e8d811cdfe3d" />
<img width="562" height="1280" alt="photo_30_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/0f4ea7b9-e8ad-41bd-b6ca-1fda416d5741" />
<img width="562" height="1280" alt="photo_29_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/877ead9f-4481-42e6-823d-d74daaf9a01d" />
<img width="562" height="1280" alt="photo_28_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/5a44e4f8-695b-4b3d-bbe8-128ee8a363aa" />
<img width="562" height="1280" alt="photo_27_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/ce42cd51-3a2e-4c4d-be15-757b9fcd0c0f" />
<img width="562" height="1280" alt="photo_26_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/7bce2baf-33a3-4da7-b793-62985300a47f" />
<img width="562" height="1280" alt="photo_25_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/8f99f016-47de-491c-b41f-f7b69bd47cbf" />
<img width="562" height="1280" alt="photo_24_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/de866ed4-6be5-47e8-a7f8-3c11d9d677fd" />
<img width="562" height="1280" alt="photo_23_2026-03-31_22-28-42" src="https://github.com/user-attachments/assets/2b2fc089-7c26-4172-ae0b-227e749ed0bc" />

