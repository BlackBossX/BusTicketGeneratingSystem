# QR Bus Ticket Generating System

🚍 Welcome to the **QR Bus Ticket Generating System**! This project streamlines bus ticket booking with modern technologies like Google Cloud APIs and QR codes.

---

## 🚀 Features

1. **🔒 Secure User Management**
    - User sign-up and login with bcrypt for password hashing.
    - Unique User IDs generated via database triggers.

2. **📍 Location-Based Ticketing**
    - Calculates distance and travel duration using Google Cloud Distance Matrix API.
    - Dynamic fare calculation based on distance.

3. **🎟️ QR Code Tickets**
    - QR codes generated for tickets using the [goqr.me API](https://goqr.me/api/).
    - Tickets can be scanned for validation.

4. **💾 Database Management**
    - MySQL database to store user and trip information.
    - Automated ID generation for users and trips using SQL triggers.

5. **🛠️ Modular and Scalable**
    - Built with Maven for dependency management.
    - Organized into clear, reusable components.



---

## 🛠️ Technologies Used

- **Java**
- **Maven**
- **Google Cloud Distance Matrix API**
- **[goqr.me API](https://goqr.me/api/)**
- **bcrypt**
- **MySQL**

---

## ⚙️ Setup Instructions

1. Clone the repository and navigate to the project directory.
2. Set up a MySQL database and execute the provided schema and triggers.
3. Configure API keys for:
    - Google Cloud Distance Matrix API.
    - [goqr.me API](https://goqr.me/api/).


4. Configure the **.env** file 
    - _**GOOGLE_MAPS_API_KEY**_
    - _**DB_URL**_
    - _**DB_USERNAME**_
    - _**DB_PASSWORD**_
    - _**QR_API_URL**_ 
   

5. Build the project with Maven:
   ```bash
   mvn clean install
   ```

---

## 📊 Database Schema

## 🧑‍🤝‍🧑Users Table
```sql
CREATE TABLE Users (
    user_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(75) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    phone VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Trigger for User ID Generation:**
```sql
DELIMITER $$
CREATE TRIGGER before_insert_users 
BEFORE INSERT ON Users 
FOR EACH ROW 
BEGIN
    DECLARE next_id INT;
    SELECT COALESCE(MAX(CAST(SUBSTRING(user_id, 2) AS UNSIGNED)), 0) + 1 INTO next_id FROM Users;
    SET NEW.user_id = CONCAT('U', LPAD(next_id, 4, '0'));
END$$;
DELIMITER ;
```

## 🛣️Trips Table
```sql
CREATE TABLE Trips (
    trip_id VARCHAR(10) PRIMARY KEY,
    start_location VARCHAR(100) NOT NULL,
    end_location VARCHAR(100) NOT NULL,
    distance VARCHAR(20) NOT NULL,
    duration VARCHAR(20) NOT NULL,
    fare DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Trigger for Trip ID Generation:**
```sql
DELIMITER $$
CREATE TRIGGER before_insert_trips 
BEFORE INSERT ON Trips 
FOR EACH ROW 
BEGIN
    DECLARE next_id INT;
    SELECT COALESCE(MAX(CAST(SUBSTRING(trip_id, 2) AS UNSIGNED)), 0) + 1 INTO next_id FROM Trips;
    SET NEW.trip_id = CONCAT('R', LPAD(next_id, 4, '0'));
END$$;
DELIMITER ;
```
## 🎟️Tickets Table
```sql
CREATE TABLE Tickets (
    ticket_id VARCHAR(10) PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    start_location VARCHAR(100) NOT NULL,
    end_location VARCHAR(100) NOT NULL,
    distance VARCHAR(20) NOT NULL,
    duration VARCHAR(20) NOT NULL,
    total_fare DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

```
**Trigger for Ticket ID Generation:**
```sql
DELIMITER $$
CREATE TRIGGER before_insert_tickets 
BEFORE INSERT ON Tickets 
FOR EACH ROW 
BEGIN
    DECLARE next_id INT;
    SELECT COALESCE(MAX(CAST(SUBSTRING(ticket_id, 2) AS UNSIGNED)), 0) + 1 INTO next_id 
    FROM Tickets;
    SET NEW.ticket_id = CONCAT('T', LPAD(next_id, 4, '0'));
END$$
DELIMITER ;
```

## 💺Seats Table
```sql
CREATE TABLE Seats (
    seat_id VARCHAR(10) NOT NULL PRIMARY KEY,
    ticket_id VARCHAR(10) NOT NULL,
    user_id VARCHAR(10) NOT NULL,
    availability TINYINT(1) NOT NULL DEFAULT 1
);
```
**Trigger for Seat ID Generation:**
```sql
DELIMITER $$
CREATE TRIGGER before_insert_seats
   BEFORE INSERT ON Seats
   FOR EACH ROW
BEGIN
   DECLARE next_id INT;
   SELECT COALESCE(MAX(CAST(SUBSTRING(seat_id, 2) AS UNSIGNED)), 0) + 1 INTO next_id
   FROM Seats;
   SET NEW.seat_id = CONCAT('S', LPAD(next_id, 4, '0'));
END;
DELIMITER ;
```
**Trigger to Limit the Seat Count to 50:**
```sql
DELIMITER $$
CREATE TRIGGER limit_seats_before_insert
   BEFORE INSERT ON Seats
   FOR EACH ROW
BEGIN
   DECLARE row_count INT;

   -- Count the current number of rows in the table
   SELECT COUNT(*) INTO row_count FROM Seats;

   -- Check if the row count exceeds 50
   IF row_count >= 50 THEN
      SIGNAL SQLSTATE '45000'
         SET MESSAGE_TEXT = 'Cannot add more rows. Maximum seat limit (50) reached.';
   END IF;
END;
DELIMITER ;
```


---

## 🌟 Future Enhancements

- Integrate a payment gateway for online payments.
- Add analytics for trip data.
- Upgrade the user interface with modern frameworks.

---



## 👤 Support


For support, email malandealwis@gmail.com 