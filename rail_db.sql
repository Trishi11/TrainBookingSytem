CREATE DATABASE rail_db;
USE rail_db;

CREATE TABLE trains (
  train_id INT PRIMARY KEY AUTO_INCREMENT,
  train_name VARCHAR(100) NOT NULL,
  train_type VARCHAR(50) NOT NULL,
  total_seats INT NOT NULL,
  fare_per_km DECIMAL(8,2) NOT NULL
);

CREATE TABLE stations (
  station_id INT PRIMARY KEY AUTO_INCREMENT,
  station_name VARCHAR(100) NOT NULL,
  city VARCHAR(50) NOT NULL
);

CREATE TABLE bookings (
  booking_id INT PRIMARY KEY AUTO_INCREMENT,
  train_id INT NOT NULL,
  source_station VARCHAR(100) NOT NULL,
  destination_station VARCHAR(100) NOT NULL,
  date_of_journey DATE NOT NULL,
  seat_count INT NOT NULL,
  total_fare DECIMAL(10,2) NOT NULL,
  passenger_name VARCHAR(100) NOT NULL,
  passenger_contact VARCHAR(20),
  FOREIGN KEY (train_id) REFERENCES trains(train_id)
);

INSERT INTO stations (station_name, city) VALUES ('Mumbai Central','Mumbai'), ('Pune Jn','Pune'), ('Vadodara','Vadodara');
INSERT INTO trains (train_name, train_type, total_seats, fare_per_km) VALUES ('Express A','Express',200,1.5), ('Intercity B','Intercity',120,1.2);
