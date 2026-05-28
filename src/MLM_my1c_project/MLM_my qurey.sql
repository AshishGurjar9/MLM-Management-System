
---------   RADHE RADHE ---------
CREATE DATABASE MLM;    
USE MLM;
------------------------------------  RADHE RADHE 
CREATE TABLE register (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,

    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
                                                    -- 1
    referral_code VARCHAR(20) UNIQUE NOT NULL,      -- MLM@1, MLM@2...
    parent_referral_code VARCHAR(20) DEFAULT NULL,  -- jis se join hua
    role VARCHAR(10) DEFAULT 'USER',

    CONSTRAINT fk_parent_referral
    FOREIGN KEY (parent_referral_code)
    REFERENCES register(referral_code)
);

select * from register;

------------------------------------------------------------
CREATE TABLE commission_plan (
    level INT,
    commission_percent INT
); 
select * FROM commission_plan;

alter table commission_plan         -- 3
MODIFY commission_percent double;

alter table commission_plan
rename column commission_percent to commission_amount ;
-----------------------------------------------
CREATE TABLE wallet (
    user_id INT,
    balance INT               -- 4 
);
--------------------------------------------------------------------
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,

    user_id INT NOT NULL,              -- jiska wallet affect hua    5
    amount DECIMAL(10,2) NOT NULL,

    transaction_type VARCHAR(15) NOT NULL,
    -- DEPOSIT / WITHDRAW / COMMISSION

    level INT DEFAULT NULL,             -- commission kis level se
    from_user_id INT DEFAULT NULL,      -- kis user se commission aayi

    status VARCHAR(15) DEFAULT 'SUCCESS',
    -- SUCCESS / PENDING / REJECTED

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
------------------------------------------------------------------------
CREATE TABLE commission (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    from_user_id INT,               -- 6
    level INT,
    amount INT
);
 alter table commission 
 add created timestamp default current_timestamp;
---------------------------------------------------------------------------------
CREATE TABLE withdrawal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    amount INT,
    status VARCHAR(15)   -- PENDING / APPROVED / REJECTED    7
   
);
------------------------------------------------------------------------------------------
CREATE TABLE deposit (
    id INT AUTO_INCREMENT PRIMARY KEY,    --   8
    user_id INT,
    amount INT,
    deposit_date VARCHAR(30)  
);
--------------------------------------------------------------------------------------------
-----------
ALTER TABLE wallet
MODIFY balance DECIMAL(10,2) DEFAULT 0;
ALTER TABLE wallet
ADD PRIMARY KEY (user_id);

ALTER TABLE wallet
ADD CONSTRAINT fk_wallet_user
FOREIGN KEY (user_id)
REFERENCES register(user_id)
ON DELETE CASCADE;


---------
ALTER TABLE transactions
add FOREIGN KEY (user_id) REFERENCES register(user_id),
 add   FOREIGN KEY (from_user_id) REFERENCES register(user_id);

----------
ALTER TABLE commission_plan
ADD PRIMARY KEY (level);

ALTER TABLE commission
ADD FOREIGN KEY (user_id)      REFERENCES register(user_id),
ADD FOREIGN KEY (from_user_id) REFERENCES register(user_id),
ADD FOREIGN KEY (level)        REFERENCES commission_plan(level);

--------
ALTER TABLE withdrawal
ADD FOREIGN KEY (user_id) REFERENCES register(user_id);

----------
ALTER TABLE deposit
ADD FOREIGN KEY (user_id) REFERENCES register(user_id);
 









ALTER TABLE wallet
ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE transactions
RENAME COLUMN old_column_name TO new_column_name;

alter table deposit
ADD status VARCHAR(20) DEFAULT 'PENDING';
ALTER TABLE commission_plan
 ADD deposit_commi_perent DOUBLE DEFAULT 3;

ALTER TABLE transactions
DROP COLUMN description;


UPDATE register
SET parent_referral_code = 'MLM@'
WHERE parent_referral_code IS NULL;

ALTER TABLE register 
MODIFY parent_referral_code VARCHAR(20)  DEFAULT NULL;

INSERT INTO register
(name, email, password, referral_code, parent_referral_code, role)
VALUES
('ADMIN', 'admin@mlm.com', 'admin123', 'MLM@0', NULL, 'ADMIN');

ALTER TABLE register ADD role VARCHAR(10);

UPDATE register SET role='ADMIN' WHERE user_id=1;
UPDATE register SET role='USER' WHERE user_id!=1;


ALTER TABLE register
DROP FOREIGN KEY refer_id;
ALTER TABLE `register`
DROP FOREIGN KEY `register_ibfk_1`;
SHOW CREATE TABLE register;

ALTER TABLE register
ADD referral_code VARCHAR(20) UNIQUE;

ALTER TABLE register ADD status VARCHAR(10) DEFAULT 'ACTIVE';

alter table withdrawal
add column  date_t timestamp default current_timestamp;

ALTER TABLE deposit
RENAME COLUMN deposit_date TO created_at;

ALTER TABLE deposit
MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE withdrawal
RENAME COLUMN date_t TO created_at;


INSERT INTO wallet (user_id, balance)
SELECT user_id, 0
FROM register
WHERE referral_code = 'MLM@0'
ON DUPLICATE KEY UPDATE balance = balance;



SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE register;

SET FOREIGN_KEY_CHECKS = 1;



