
DELETE FROM permissions_profiles;
DELETE FROM permissions;
DELETE FROM users_profiles;
DELETE FROM profiles;

DELETE FROM contacts;
DELETE FROM addresses;
DELETE FROM users;

INSERT INTO permissions(id, permission, note) VALUES (1, 'LOGIN', 'User Login');
INSERT INTO permissions(id, permission, note) VALUES (2, 'VIEW_ROLE', 'View user profile');
INSERT INTO permissions(id, permission, note) VALUES (3, 'ADMIN_USER_DATA', 'Manage user data');

INSERT INTO permissions(id, permission, note, enabled) VALUES (4, 'ADMIN_STATISTICS', 'View statistical graphs', false);

INSERT INTO profiles(id, profile) VALUES (1, 'USER');
INSERT INTO profiles(id, profile) VALUES (2, 'ADMINISTRATOR');

INSERT INTO permissions_profiles(permission_id, profile_id) VALUES (1, 1);
INSERT INTO permissions_profiles(permission_id, profile_id) VALUES (2, 1);

INSERT INTO permissions_profiles(permission_id, profile_id) VALUES (1, 2);
INSERT INTO permissions_profiles(permission_id, profile_id) VALUES (2, 2);
INSERT INTO permissions_profiles(permission_id, profile_id) VALUES (3, 2);


INSERT INTO users(id, username, password, name, surname, gender) VALUES (1, 'erdi', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Erdi', 'Test', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES (2, 'tony', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Tony', 'White', 0);
INSERT INTO users(id, username, password, name, surname, gender) VALUES (3, 'johnny', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Johnny', 'Black', 1);
INSERT INTO users(id, username, password, name, surname, gender) VALUES (4, 'johann', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Johann', 'Blue', 1);
INSERT INTO users(id, username, password, name, surname, gender) VALUES (5, 'amadeus', '1d/NZaEqNgtEomytAPrwm/+QjmbudLg33oeEk77Xh88=', 'Amadeus', 'Green', 0);

UPDATE users SET ENABLED = false WHERE id = 6;

UPDATE users SET birth_date = '1988-11-22' WHERE id = 1;
UPDATE users SET secured = true WHERE id = 1;

INSERT INTO users_profiles(user_id, profile_id) VALUES (1, 1);
INSERT INTO users_profiles(user_id, profile_id) VALUES (1, 2);

INSERT INTO users_profiles(user_id, profile_id) VALUES (2, 1);
INSERT INTO users_profiles(user_id, profile_id) VALUES (3, 1);
INSERT INTO users_profiles(user_id, profile_id) VALUES (4, 1);
INSERT INTO users_profiles(user_id, profile_id) VALUES (5, 1);



INSERT INTO contacts(user_id, email, phone, note) VALUES (1, 'erdiyilmaz@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES (2, 'tony.soprano@gmail.com', NULL, 'test contact note on tony soprano');
INSERT INTO contacts(user_id, email, phone, note) VALUES (3, 'johnny.cash@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES (4, 'johann.bach@gmail.com', NULL, NULL);
INSERT INTO contacts(user_id, email, phone, note) VALUES (5, 'amadeus.mozart@gmail.com', NULL, NULL);

insert into addresses(user_id, address, city, country, zip_code) values (2, 'Kadikoy', 'Zuhtupasa', 'Istanbul', 'Turkey', '50100');
insert into addresses(user_id, address, city, country, zip_code) values (7, 'Anittepe', 'Cankaya', 'Ankara', 'Turkey', '30100');
insert into addresses(user_id, address, city, country, zip_code) values (8, 'Moglich', 'Strasse', 'Berlin', 'Germany', '34100');
