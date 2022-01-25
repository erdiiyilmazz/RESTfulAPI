DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS addresses;

DROP TABLE IF EXISTS permissions_profiles;
DROP TABLE IF EXISTS users_profiles;

DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS users;

CREATE TABLE `profiles` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `profile` varchar(80) NOT NULL UNIQUE
);

CREATE TABLE `permissions` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `permission` varchar(80) NOT NULL UNIQUE,
  enabled TINYINT DEFAULT '1',
  note varchar(255) DEFAULT NULL
);

CREATE TABLE permissions_profiles (
  `permission_id` BIGINT(20) NOT NULL,
  `profile_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (permission_id, profile_id),
  FOREIGN KEY (permission_id) REFERENCES permissions(id),
  FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

CREATE TABLE `users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` varchar(100) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  gender TINYINT DEFAULT NULL,
  birth_date DATE DEFAULT NULL,
  enabled TINYINT DEFAULT '1',
  creation_dt timestamp NOT NULL DEFAULT current_timestamp,
  updated_dt timestamp DEFAULT current_timestamp,
  login_dt timestamp NULL,
  note varchar(255) DEFAULT NULL,
  secured TINYINT DEFAULT '0'
);

CREATE TABLE users_profiles (
  `user_id` BIGINT(20) NOT NULL,
  `profile_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (user_id, profile_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

CREATE TABLE `contacts` (
  user_id BIGINT(20) NOT NULL PRIMARY KEY,
  `email` varchar(255) NOT NULL UNIQUE,
  `phone` varchar(20) DEFAULT NULL,
  facebook varchar(255) DEFAULT NULL,
  linkedin varchar(255) DEFAULT NULL,
  website varchar(255) DEFAULT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE `addresses` (
  user_id BIGINT(20) NOT NULL PRIMARY KEY,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `zip_code` varchar(20) DEFAULT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

DROP VIEW IF EXISTS enabled_users;

CREATE VIEW enabled_users AS
SELECT username, contacts.email, contacts.phone,creation_dt, updated_dt, login_dt, secured
FROM users
INNER JOIN contacts on contacts.user_id = users.id
WHERE
enabled IS TRUE;
