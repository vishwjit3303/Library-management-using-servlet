# Book Library Management System  

## Description  
The **Book Library Management System** is a web-based application designed to manage library resources such as books, borrowers, and users efficiently. It provides essential features like adding books, registering users, managing borrow records, and fetching necessary data from the database. The project is implemented using Java Servlets for backend logic and JDBC for database connectivity.  

---

## Features  
1. **Add Books**  
   - Add new books to the library database using `AddBookServlet`.  

2. **Add Borrowers**  
   - Register borrowers with necessary details via `AddBorrowerServlet`.  

3. **User Management**  
   - Add and manage library users with `AddUserServlet`.  

4. **Data Retrieval**  
   - Fetch book records using `FetchRecordsBook` servlet.  
   - Retrieve specific book details with `RequiredBookData`.  

5. **User Authentication**  
   - Login functionality via `LoginPage`.  
   - Signup for new users through `SignupPage`.  

6. **User Details**  
   - Manage and fetch user details via `UserDetailsServlet`.  

---

## Technologies Used  
1. **Frontend**  
   - HTML, CSS, and JavaScript (for forms and user interface).  

2. **Backend**  
   - Java Servlets.  

3. **Database**  
   - MySQL Database connected via JDBC.  

4. **Server**  
   - Deployed on Apache Tomcat or any other servlet container.  

---

## Prerequisites  
1. Java Development Kit (JDK) 8 or higher.  
2. Apache Tomcat server.  
3. SQL Database (e.g., MySQL or PostgreSQL).  
4. JDBC Driver for SQL database.  

---

## Installation and Setup  

### Step 1: Clone the Repository  
```bash  
git clone https://github.com/your-repo/book-library-management.git  
cd book-library-management  
```  

### Step 2: Configure the Database  
1. Create a SQL database (e.g., `library_management`).  
2. Run the provided SQL script to set up tables.  
3. Update the database connection details in the JDBC configuration file (`db.properties`).  

### Step 3: Deploy the Application  
1. Import the project into an IDE (e.g., Eclipse or IntelliJ).  
2. Build the project and deploy the WAR file to Tomcat.  

### Step 4: Start the Server  
- Start the Tomcat server and access the application at `http://localhost:8080/book-library-management`.  

---

## Project Structure  
```plaintext  
/book-library-management  
├── src  
│   ├── main  
│   │   ├── AddBookServlet.java  
│   │   ├── AddBorrowerServlet.java  
│   │   ├── AddUserServlet.java  
│   │   ├── FetchRecordsBook.java  
│   │   ├── RequiredBookData.java  
│   │   ├── LoginPage.java  
│   │   ├── SignupPage.java  
│   │   ├── UserDetailsServlet.java  
├── build  
├── SQL  
│   ├── library_management.sql  
```  

---

- Output
- <img src = "output screenshots/s6.png" alt = "1 image"> 
- <img src = "output screenshots/s7.png" alt = "2 image"> 
- <img src = "output screenshots/s8.png" alt = "3 image"> 
- <img src = "output screenshots/s5.png" alt = "4 image"> 
- <img src = "output screenshots/s4.png" alt = "5 image"> 
- <img src = "output screenshots/s3.png" alt = "6 image"> 
- <img src = "output screenshots/s2.png" alt = "7 image"> 
- <img src = "output screenshots/s1.png" alt = "8 image"> 


## Usage  
1. **Admin:**  
   - Add books and borrowers.  
   - Manage user data.  

2. **User:**  
   - Register for an account.  
   - Login and view book details.  

---

## Contribution  
1. Fork the repository.  
2. Create a new branch (`feature-branch-name`).  
3. Submit a pull request with detailed changes.  

---

## License  
This project is licensed under the [MIT License](LICENSE).  

---  

Let me know if you need to customize it further!
