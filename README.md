**🎵Album Manager Application**

Java | Java Swing | MySQL | JDBC | MVC | NetBeans

A GUI-based application built using Java Swing with a MySQL backend, following industry-standard MVC and DAO architecture.

The system enables secure user authentication and complete management of albums, tracks, artists, and users with persistent database storage.

This project demonstrates strong fundamentals in Java, OOP, database integration, and clean software design, making it suitable for real-world desktop applications.


🚀Highlights (Recruiter View)

Designed a multi-module desktop application using Java Swing
Implemented MVC + DAO architecture for clean separation of concerns
Integrated MySQL database using JDBC for persistent data storage
Built a secure login & registration system
Implemented full CRUD operations across multiple entities
Developed reusable, modular UI dialogs using Swing
Structured the project for scalability and maintainability

🧩 Core Features

🔐 Authentication System
Login & Registration
Database-backed user validation

🎶 Album Management
Add, update, delete, and view albums
Structured album data using model classes

🎧 Track Management
Manage tracks associated with albums

👤 User Management
Admin-level user handling

📊 Analytics & Activity Views
Centralized management dialogs

🗄️ Database Persistence
MySQL with JDBC connection handling

🧠 Architecture & Design
This project strictly follows MVC Architecture:

Model → Domain entities (Album, Track, User)
View → Java Swing UI (JFrame, JDialog)
Controller → Business logic & event handling
DAO Layer → Database interaction abstraction
DB Layer → Centralized JDBC connection management


🗂️ Project Structure
src
├── controller   → Handles user actions & flow
├── dao          → Database CRUD operations
├── db           → JDBC connection management
├── model        → Data models (POJOs)
├── view         → Swing UI components
└── App.java     → Application entry point


🛠️ Tech Stack
Language: Java (JDK 8+)
UI: Java Swing
Database: MySQL
Connectivity: JDBC
IDE: NetBeans
Version Control: Git, GitHub


⚙️ How to Run

1️⃣ Clone the Repository
git clone https://github.com/your-username/AlbumManagerProject.git

2️⃣ Configure MySQL Database
Create database:
CREATE DATABASE album_manager;


Update credentials in:
db/DatabaseManager.java

3️⃣ Run the Application
Open project in NetBeans
Run App.java


🔮 Future Improvements

Role-based access control
Search and filtering
Album cover image support
Migration to JavaFX
Report export functionality

👩‍💻 Author
Gayatri Bhakta
Computer Science Student | Aspiring Software Engineer
