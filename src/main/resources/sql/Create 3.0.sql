DROP DATABASE timestar;

CREATE DATABASE timestar;

USE timestar;

CREATE TABLE partitions (
id INT NOT NULL UNIQUE, 
name VARCHAR(10) NOT NULL UNIQUE, 
PRIMARY KEY (id));

CREATE TABLE account (
id INT NOT NULL, 
enabled TINYINT NOT NULL DEFAULT 1, 
password CHAR(60) NOT NULL, 
account_type VARCHAR(10) NOT NULL, 
username VARCHAR(60) NOT NULL UNIQUE, 
PRIMARY KEY(username));

CREATE TABLE roles (
username VARCHAR(60) NOT NULL, 
role VARCHAR(60) NOT NULL, 
FOREIGN KEY(username) REFERENCES account(username));

INSERT INTO partitions (id, name)
VALUES (1, 'COTEM');

INSERT INTO account (username, password, account_type, id) 
VALUES ('1.goodlike', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ADMIN', 0);

INSERT INTO roles (username, role) 
VALUES ('1.goodlike', 'ROLE_ADMIN');

INSERT INTO roles (username, role) 
VALUES ('1.goodlike', 'ROLE_TEACHER');

CREATE TABLE teacher ( 
partition_id INT NOT NULL, 
id INT NOT NULL AUTO_INCREMENT, 
payment_day TINYINT NOT NULL, 
email VARCHAR(60) NOT NULL, 
name VARCHAR(30) NOT NULL, 
surname VARCHAR(30) NOT NULL, 
phone VARCHAR(30) NOT NULL, 
city VARCHAR(30) NOT NULL, 
picture_name VARCHAR(100) NOT NULL, 
document_name VARCHAR(100) NOT NULL, 
comment_about VARCHAR(500) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id), 
UNIQUE KEY(partition_id, email));

CREATE TABLE teacher_language ( 
partition_id INT NOT NULL, 
teacher_id INT NOT NULL, 
code VARCHAR(3) NOT NULL, 
FOREIGN KEY(teacher_id) REFERENCES teacher(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id), 
UNIQUE KEY (teacher_id, code));

CREATE TABLE customer ( 
partition_id INT NOT NULL, 
id INT NOT NULL AUTO_INCREMENT, 
payment_day TINYINT NOT NULL, 
start_date DATE NOT NULL, 
payment_value DECIMAL(19, 4) NOT NULL, 
name VARCHAR(30) NOT NULL, 
phone VARCHAR(30) NOT NULL, 
website VARCHAR(30) NOT NULL, 
picture_name VARCHAR(100) NOT NULL, 
comment_about VARCHAR(500) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id));

CREATE TABLE customer_lang ( 
partition_id INT NOT NULL, 
customer_id INT NOT NULL, 
language_level VARCHAR(20) NOT NULL, 
FOREIGN KEY(customer_id) REFERENCES customer(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id), 
UNIQUE KEY (customer_id, language_level));

CREATE TABLE student_group ( 
partition_id INT NOT NULL, 
id INT NOT NULL AUTO_INCREMENT, 
teacher_id INT, 
name VARCHAR(30) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(teacher_id) REFERENCES teacher(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id));

CREATE TABLE student ( 
partition_id INT NOT NULL, 
id INT NOT NULL AUTO_INCREMENT, 
group_id INT NOT NULL, 
customer_id INT NOT NULL, 
email VARCHAR(60) NOT NULL UNIQUE, 
name VARCHAR(60) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(group_id) REFERENCES student_group(id), 
FOREIGN KEY(customer_id) REFERENCES customer(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id));

CREATE TABLE lesson ( 
partition_id INT NOT NULL, 
id BIGINT NOT NULL AUTO_INCREMENT, 
teacher_id INT NOT NULL, 
group_id INT NOT NULL, 
date_of_lesson DATE NOT NULL, 
time_of_start SMALLINT NOT NULL, 
time_of_end SMALLINT NOT NULL, 
length_in_minutes SMALLINT NOT NULL, 
comment_about VARCHAR(500) NOT NULL, 
PRIMARY KEY(id), 
FOREIGN KEY(teacher_id) REFERENCES teacher(id), 
FOREIGN KEY(group_id) REFERENCES student_group(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id));

CREATE TABLE lesson_attendance ( 
partition_id INT NOT NULL, 
lesson_id BIGINT NOT NULL, 
student_id INT NOT NULL, 
FOREIGN KEY(lesson_id) REFERENCES lesson(id), 
FOREIGN KEY(student_id) REFERENCES student(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id), 
UNIQUE KEY (lesson_id, student_id));

CREATE TABLE lesson_code ( 
partition_id INT NOT NULL, 
lesson_id BIGINT NOT NULL, 
student_id INT NOT NULL, 
code INT NOT NULL, 
FOREIGN KEY(lesson_id) REFERENCES lesson(id), 
FOREIGN KEY(student_id) REFERENCES student(id), 
FOREIGN KEY(partition_id) REFERENCES partitions(id));
