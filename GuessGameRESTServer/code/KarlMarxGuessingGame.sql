DROP DATABASE IF EXISTS Hotel;

CREATE DATABASE Hotel;

USE Hotel;

CREATE TABLE `Hotel`.`amenities` (
    `amenity_id` INT NOT NULL AUTO_INCREMENT,
    `amenity_name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`amenity_id`)
);
  
CREATE TABLE `Hotel`.`room_types` (
    `room_type_id` INT NOT NULL AUTO_INCREMENT,
    `type_name` VARCHAR(15) NOT NULL,
    `base_price` DECIMAL(7 , 2 ) NOT NULL,
    `extra_person_price` DECIMAL(7 , 2 ) NOT NULL DEFAULT '0.00',
    `adults_in_base` TINYINT(2) NOT NULL,
    `number_queen_beds` TINYINT(1) NOT NULL DEFAULT 0,
    `number_king_beds` TINYINT(1) NOT NULL DEFAULT 0,
    `number_sleeper_sofas` TINYINT(1) NOT NULL DEFAULT 0,
    `maximum_occupancy` TINYINT(2) NOT NULL,
    `standard_occupancy` TINYINT(2) NOT NULL,
    PRIMARY KEY (`room_type_id`)
);
  
CREATE TABLE `Hotel`.`room` (
    `room_number` SMALLINT(4) NOT NULL,
    `room_type_id` INT NOT NULL,
    `has_jacuzzi` TINYINT(1) NOT NULL DEFAULT 0,
    `is_ADA_accessible` TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`room_number`),
    FOREIGN KEY (`room_type_id`)
        REFERENCES `room_types` (`room_type_id`)
);
CREATE TABLE `Hotel`.`guest` (
    `guest_id` INT NOT NULL AUTO_INCREMENT,
    `given_name` VARCHAR(60),
    `surname` VARCHAR(60),
    `address` VARCHAR(120),
    `city` VARCHAR(60),
    `state` CHAR(2),
    `zip` CHAR(5),
    `phone` CHAR(14),
    PRIMARY KEY (`guest_id`)
);
CREATE TABLE `Hotel`.`booking` (
    booking_id INT NOT NULL AUTO_INCREMENT,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    adult_number TINYINT(2) NOT NULL,
    child_number TINYINT(2) NOT NULL,
    guest_id INT NOT NULL,
    price DECIMAL(10 , 2 ),
    room_number SMALLINT(4) NOT NULL,
    PRIMARY KEY (`booking_id`),
    FOREIGN KEY (`guest_id`)
        REFERENCES `guest` (`guest_id`),
    FOREIGN KEY (`room_number`)
        REFERENCES `room` (`room_number`)
);
CREATE TABLE `Hotel`.`amenities_in_rooms` (
    room_number SMALLINT(4) NOT NULL,
    amenity_id INT NOT NULL,
    PRIMARY KEY (`room_number` , `amenity_id`)
);
ALTER TABLE `Hotel`.`amenities_in_rooms` 
ADD CONSTRAINT fk_amenitiesinrooms_rooms
FOREIGN KEY (`room_number`)
        REFERENCES `room` (`room_number`),
ADD CONSTRAINT fk_amenitiesinrooms_amenities 
    FOREIGN KEY (`amenity_id`)
        REFERENCES `amenities` (`amenity_id`)
        ;

    
    

   
