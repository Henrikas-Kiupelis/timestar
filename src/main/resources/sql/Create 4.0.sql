DROP DATABASE timestar;

CREATE DATABASE timestar;

USE timestar;

CREATE TABLE partitions (
  id INT NOT NULL UNIQUE,
  name VARCHAR(10) NOT NULL UNIQUE,
  PRIMARY KEY (id));

CREATE TABLE account (
  id INT NOT NULL,
  enabled BIT NOT NULL DEFAULT 1,
  created_at BIGINT,
  updated_at BIGINT,
  password CHAR(60) NOT NULL,
  account_type VARCHAR(10) NOT NULL,
  username VARCHAR(60) NOT NULL UNIQUE,
  PRIMARY KEY(username));

DELIMITER //
CREATE TRIGGER automatic_roles
AFTER INSERT ON account
FOR EACH ROW
  BEGIN
    IF NEW.account_type = 'ADMIN'
    THEN
      INSERT roles (username, role)
      VALUES (NEW.username, 'ROLE_ADMIN');
    END IF;
    INSERT roles (username, role)
    VALUES (NEW.username, 'ROLE_TEACHER');
  END; //

CREATE TRIGGER automatic_roles_deletion
BEFORE DELETE ON account
FOR EACH ROW
  DELETE FROM roles
  WHERE roles.username = OLD.username;

CREATE TRIGGER create_timestamps_inserting_account
BEFORE INSERT ON account
FOR EACH ROW
  BEGIN
    SET NEW.created_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_account
BEFORE UPDATE ON account
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE roles (
  username VARCHAR(60) NOT NULL,
  role VARCHAR(60) NOT NULL,
  FOREIGN KEY(username) REFERENCES account(username));

INSERT INTO partitions (id, name)
VALUES (0, 'TEST');

INSERT INTO partitions (id, name)
VALUES (1, 'DEV');

INSERT INTO account (username, password, account_type, id)
VALUES ('0.test', '$2a$10$ReQmCgAd1YqDMHNg5zg7hOj.uzJhAACGRkMSMV04h6iaTzxhfTC.6', 'ADMIN', 0);

INSERT INTO account (username, password, account_type, id)
VALUES ('1.goodlike', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ADMIN', 0);

CREATE TABLE teacher (
  partition_id INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  created_at BIGINT,
  updated_at BIGINT,

  payment_day INT NOT NULL,
  hourly_wage DECIMAL(19, 4) NOT NULL,
  academic_wage DECIMAL(19, 4) NOT NULL,
  name VARCHAR(30) NOT NULL,
  surname VARCHAR(30) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  city VARCHAR(30) NOT NULL,
  email VARCHAR(60) NOT NULL,
  picture VARCHAR(100) NOT NULL,
  document VARCHAR(100) NOT NULL,
  comment VARCHAR(500) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id),
  UNIQUE KEY(partition_id, email));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_teacher
BEFORE INSERT ON teacher
FOR EACH ROW
  BEGIN
    SET NEW.created_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_teacher
BEFORE UPDATE ON teacher
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

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
  created_at BIGINT,
  updated_at BIGINT,

  start_date DATE NOT NULL,
  name VARCHAR(30) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  website VARCHAR(30) NOT NULL,
  picture VARCHAR(100) NOT NULL,
  comment VARCHAR(500) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_customer
BEFORE INSERT ON customer
FOR EACH ROW
  BEGIN
    SET NEW.created_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_customer
BEFORE UPDATE ON customer
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE group_of_students (
  partition_id INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  created_at BIGINT,
  updated_at BIGINT,

  customer_id INT,
  teacher_id INT NOT NULL,
  use_hourly_wage BIT NOT NULL,
  language_level VARCHAR(20) NOT NULL,
  name VARCHAR(30) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(customer_id) REFERENCES customer(id),
  FOREIGN KEY(teacher_id) REFERENCES teacher(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_group
BEFORE INSERT ON group_of_students
FOR EACH ROW
  BEGIN
    SET NEW.created_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_group
BEFORE UPDATE ON group_of_students
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE student (
  partition_id INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  created_at BIGINT,
  updated_at BIGINT,

  code INT,
  customer_id INT,
  start_date DATE NOT NULL,
  email VARCHAR(60) NOT NULL,
  name VARCHAR(60) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(customer_id) REFERENCES customer(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id),
  UNIQUE KEY(partition_id, email));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_student
BEFORE INSERT ON student
FOR EACH ROW
  BEGIN
    SET NEW.created_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_student
BEFORE UPDATE ON student
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE students_in_groups (
  partition_id INT NOT NULL,
  student_id INT NOT NULL,
  group_id INT NOT NULL,
  FOREIGN KEY(student_id) REFERENCES student(id),
  FOREIGN KEY(group_id) REFERENCES group_of_students(id),
  UNIQUE KEY (student_id, group_id));

CREATE TABLE lesson (
  partition_id INT NOT NULL,
  id BIGINT NOT NULL AUTO_INCREMENT,
  created_at BIGINT,
  updated_at BIGINT,

  teacher_id INT,
  group_id INT NOT NULL,
  time_of_start BIGINT NOT NULL,
  time_of_end BIGINT,
  duration_in_minutes INT NOT NULL,
  comment VARCHAR(500) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(teacher_id) REFERENCES teacher(id),
  FOREIGN KEY(group_id) REFERENCES group_of_students(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_lesson
BEFORE INSERT ON lesson
FOR EACH ROW
  BEGIN
    SET NEW.created_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    SET NEW.time_of_end = NEW.time_of_start + (NEW.duration_in_minutes * 60000);
    SET NEW.teacher_id = (SELECT group_of_students.teacher_id FROM group_of_students
    WHERE group_of_students.id = NEW.group_id LIMIT 1);
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_lesson
BEFORE UPDATE ON lesson
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = UNIX_TIMESTAMP() * 1000;
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
    IF NEW.group_id != OLD.group_id THEN
      SET NEW.teacher_id = (SELECT group_of_students.teacher_id FROM group_of_students
      WHERE group_of_students.id = NEW.group_id LIMIT 1);
    END IF;
  END; //
DELIMITER ;

CREATE TABLE lesson_attendance (
  partition_id INT NOT NULL,
  lesson_id BIGINT NOT NULL,
  student_id INT NOT NULL,
  FOREIGN KEY(lesson_id) REFERENCES lesson(id),
  FOREIGN KEY(student_id) REFERENCES student(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id),
  UNIQUE KEY (lesson_id, student_id));
