DROP DATABASE timestar;

CREATE DATABASE timestar;

USE timestar;

CREATE TABLE partitions (
  id INT NOT NULL UNIQUE,
  name VARCHAR(10) NOT NULL UNIQUE,
  PRIMARY KEY (id));

CREATE TABLE account (
  username VARCHAR(70) NOT NULL UNIQUE,
  enabled BIT NOT NULL DEFAULT 1,
  id INT,
  created_at BIGINT,
  updated_at BIGINT,
  password CHAR(60) NOT NULL,
  account_type VARCHAR(10) NOT NULL,
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
    SET NEW.created_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    SET NEW.updated_at = NEW.created_at;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_account
BEFORE UPDATE ON account
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
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

INSERT INTO account (username, password, account_type)
VALUES ('0.test', '$2a$10$ReQmCgAd1YqDMHNg5zg7hOj.uzJhAACGRkMSMV04h6iaTzxhfTC.6', 'ADMIN');

INSERT INTO account (username, password, account_type)
VALUES ('1.goodlike', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ADMIN');

CREATE TABLE teacher (
  id INT NOT NULL AUTO_INCREMENT,
  partition_id INT NOT NULL,

  created_at BIGINT,
  updated_at BIGINT,

  payment_day INT NOT NULL,
  hourly_wage DECIMAL(19, 4) NOT NULL,
  academic_wage DECIMAL(19, 4) NOT NULL,
  email VARCHAR(60) NOT NULL,
  name VARCHAR(30) NOT NULL,
  surname VARCHAR(30) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  city VARCHAR(30) NOT NULL,
  picture VARCHAR(100),
  document VARCHAR(100),
  comment VARCHAR(500),
  PRIMARY KEY(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id),
  UNIQUE KEY(partition_id, email));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_teacher
BEFORE INSERT ON teacher
FOR EACH ROW
  BEGIN
    SET NEW.created_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    SET NEW.updated_at = NEW.created_at;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_teacher
BEFORE UPDATE ON teacher
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
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
  id INT NOT NULL AUTO_INCREMENT,
  partition_id INT NOT NULL,

  created_at BIGINT,
  updated_at BIGINT,

  start_date DATE NOT NULL,
  name VARCHAR(30) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  website VARCHAR(30) NOT NULL,
  picture VARCHAR(100),
  comment VARCHAR(500),
  PRIMARY KEY(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_customer
BEFORE INSERT ON customer
FOR EACH ROW
  BEGIN
    SET NEW.created_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    SET NEW.updated_at = NEW.created_at;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_customer
BEFORE UPDATE ON customer
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE group_of_students (
  id INT NOT NULL AUTO_INCREMENT,
  partition_id INT NOT NULL,
  customer_id INT,
  teacher_id INT NOT NULL,

  created_at BIGINT,
  updated_at BIGINT,

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
    SET NEW.created_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    SET NEW.updated_at = NEW.created_at;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_group_update_lesson
BEFORE UPDATE ON group_of_students
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
    IF NEW.teacher_id != OLD.teacher_id THEN
      UPDATE lesson
      SET lesson.teacher_id = NEW.teacher_id
      WHERE lesson.group_id = OLD.id;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE student (
  id INT NOT NULL AUTO_INCREMENT,
  partition_id INT NOT NULL,
  customer_id INT,

  created_at BIGINT,
  updated_at BIGINT,

  code INT,
  start_date DATE,
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
    SET NEW.created_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    SET NEW.updated_at = NEW.created_at;
  END; //

CREATE TRIGGER update_timestamp_ensure_create_immutable_student
BEFORE UPDATE ON student
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    IF NEW.created_at != OLD.created_at THEN
      SET NEW.created_at = OLD.created_at;
    END IF;
  END; //
DELIMITER ;

CREATE TABLE students_in_groups (
  student_id INT NOT NULL,
  group_id INT NOT NULL,
  partition_id INT NOT NULL,
  FOREIGN KEY(student_id) REFERENCES student(id),
  FOREIGN KEY(group_id) REFERENCES group_of_students(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id),
  UNIQUE KEY (student_id, group_id));

CREATE TABLE lesson (
  id BIGINT NOT NULL AUTO_INCREMENT,
  partition_id INT NOT NULL,
  teacher_id INT,
  group_id INT NOT NULL,

  created_at BIGINT,
  updated_at BIGINT,

  time_of_start BIGINT NOT NULL,
  time_of_end BIGINT NOT NULL,
  duration_in_minutes INT NOT NULL,
  comment VARCHAR(500),
  PRIMARY KEY(id),
  FOREIGN KEY(teacher_id) REFERENCES teacher(id),
  FOREIGN KEY(group_id) REFERENCES group_of_students(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id));

DELIMITER //
CREATE TRIGGER create_timestamps_inserting_lesson
BEFORE INSERT ON lesson
FOR EACH ROW
  BEGIN
    SET NEW.created_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
    SET NEW.updated_at = NEW.created_at;
    SET NEW.teacher_id = (SELECT group_of_students.teacher_id FROM group_of_students
    WHERE group_of_students.id = NEW.group_id LIMIT 1);
  END; //

CREATE TRIGGER update_teacher_id_and_timestamp_ensure_create_immutable_lesson
BEFORE UPDATE ON lesson
FOR EACH ROW
  BEGIN
    SET NEW.updated_at = ROUND(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000);
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
  lesson_id BIGINT NOT NULL,
  student_id INT NOT NULL,
  partition_id INT NOT NULL,
  FOREIGN KEY(lesson_id) REFERENCES lesson(id),
  FOREIGN KEY(student_id) REFERENCES student(id),
  FOREIGN KEY(partition_id) REFERENCES partitions(id),
  UNIQUE KEY (lesson_id, student_id));
