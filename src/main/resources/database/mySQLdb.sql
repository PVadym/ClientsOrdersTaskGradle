DROP DATABASE IF EXISTS clientsDB;
CREATE DATABASE IF NOT EXISTS clientsDB
  DEFAULT CHARACTER SET utf8;
USE clientsDB;

CREATE TABLE `clients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `surname` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `birthday` date NOT NULL,
  `gender` varchar(255) NOT NULL,
  `ident_number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_date` date NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `amount` double NOT NULL,
  `currency` varchar(255) NOT NULL ,
  `confirmation` bit(1) DEFAULT FALSE ,
  `client_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm2dep9derpoaehshbkkatam3v` (`client_id`),
  CONSTRAINT `FKm2dep9derpoaehshbkkatam3v` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE ,
  password VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;;


