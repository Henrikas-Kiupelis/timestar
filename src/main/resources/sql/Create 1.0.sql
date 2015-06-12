DROP DATABASE timestar;

CREATE DATABASE timestar;

USE timestar;

ALTER DATABASE timestar CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE teacher ( 
id INT NOT NULL AUTO_INCREMENT, 
name VARCHAR(30) NOT NULL, 
surname VARCHAR(30) NOT NULL, 
phone VARCHAR(30) NOT NULL, 
PRIMARY KEY(id));

ALTER TABLE teacher CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE languages ( 
teacher_id INT NOT NULL, 
code VARCHAR(3) NOT NULL, 
FOREIGN KEY(teacher_id) REFERENCES teacher(id), 
UNIQUE KEY (teacher_id, code));

ALTER TABLE languages CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE customer ( 
id INT NOT NULL AUTO_INCREMENT, 
name VARCHAR(30) NOT NULL UNIQUE, 
phone VARCHAR(30) NOT NULL, 
PRIMARY KEY(id));

ALTER TABLE customer CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE student_group ( 
id INT NOT NULL AUTO_INCREMENT, 
customer_id INT NOT NULL, 
name VARCHAR(30) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(customer_id) REFERENCES customer(id));

ALTER TABLE student_group CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE student ( 
id INT NOT NULL AUTO_INCREMENT, 
customer_id INT NOT NULL, 
group_id INT NOT NULL, 
name VARCHAR(60) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(customer_id) REFERENCES customer(id), 
FOREIGN KEY(group_id) REFERENCES student_group(id));

ALTER TABLE student CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE contract ( 
id INT NOT NULL AUTO_INCREMENT, 
teacher_id INT NOT NULL, 
customer_id INT NOT NULL, 
payment_day TINYINT NOT NULL, 
payment_value DECIMAL(19, 4) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(teacher_id) REFERENCES teacher(id), 
FOREIGN KEY(customer_id) REFERENCES customer(id), 
UNIQUE KEY (teacher_id, customer_id));

ALTER TABLE contract CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE lesson ( 
id BIGINT NOT NULL AUTO_INCREMENT, 
teacher_id INT NOT NULL, 
customer_id INT NOT NULL, 
group_id INT NOT NULL, 
date_of_lesson DATE NOT NULL, 
time_of_lesson SMALLINT NOT NULL, 
length_in_minutes SMALLINT NOT NULL, 
comment_by_teacher VARCHAR(500), 
PRIMARY KEY(id), 
FOREIGN KEY(teacher_id) REFERENCES teacher(id), 
FOREIGN KEY(customer_id) REFERENCES customer(id), 
FOREIGN KEY(group_id) REFERENCES student_group(id));

ALTER TABLE lesson CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
