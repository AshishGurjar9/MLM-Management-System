
-- Radhe Radhe --

-- table `register` 1


CREATE TABLE `register` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `referral_code` varchar(20) NOT NULL,
  `parent_referral_code` varchar(20) DEFAULT NULL,
  `role` varchar(10) DEFAULT 'USER',
  `status` varchar(10) DEFAULT 'ACTIVE',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `referral_code` (`referral_code`),
  KEY `fk_parent_referral` (`parent_referral_code`),
  CONSTRAINT `fk_parent_referral` FOREIGN KEY (`parent_referral_code`) REFERENCES `register` (`referral_code`)
) ;

INSERT INTO `register` VALUES (1,'ADMIN','admin@mlm.com','admin123','MLM@0',NULL,'ADMIN','ACTIVE');



--  commssion_plan table   2

CREATE TABLE `commission_plan` (
  `level` int NOT NULL,
  `commission_amount` double DEFAULT NULL,
  `deposit_commi_perent` double DEFAULT '3',
  PRIMARY KEY (`level`)
) ;


--   commission table   3

CREATE TABLE `commission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `from_user_id` int DEFAULT NULL,
  `level` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `commission_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `from_user_id` (`from_user_id`),
  KEY `level` (`level`),
  CONSTRAINT `commission_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `register` (`user_id`),
  CONSTRAINT `commission_ibfk_2` FOREIGN KEY (`from_user_id`) REFERENCES `register` (`user_id`),
  CONSTRAINT `commission_ibfk_3` FOREIGN KEY (`level`) REFERENCES `commission_plan` (`level`)
);



-- deposit table   4
 
CREATE TABLE `deposit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `deposit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `register` (`user_id`)
) ;



--    `transactions   5

CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `transaction_type` varchar(15) NOT NULL,
  `level` int DEFAULT NULL,
  `from_user_id` int DEFAULT NULL,
  `status` varchar(15) DEFAULT 'SUCCESS',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transaction_id`)
);


-- `wallet`  --  6

CREATE TABLE `wallet` (
  `user_id` int NOT NULL,
  `balance` decimal(10,2) DEFAULT '0.00',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `wallet_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `register` (`user_id`)
) ;


-- withdorawal table   7

CREATE TABLE `withdrawal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `withdrawal_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `register` (`user_id`)
) ;
use mlm;
truncate table register;



