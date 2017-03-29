SELECT 1;

DELETE FROM user_role;
DELETE FROM role;
DELETE FROM user;

INSERT INTO role (id, created_by, object_id, version, name) VALUES (-100, -1, 'ROLE-OBJ-100', 1, 'ADMIN');
INSERT INTO role (id, created_by, object_id, version, name) VALUES (-101, -1, 'ROLE-OBJ-101', 1, 'USER');

INSERT INTO user (id, created_by, object_id, version, email, enabled, password, display_name) VALUES (-100, -1, 'USER-OBJ-100', 1, 'user@mail.com', 1, 'password', 'reg_user');
INSERT INTO user (id, created_by, object_id, version, email, enabled, password, display_name) VALUES (-101, -1, 'USER-OBJ-101', 1, 'admin@mail.com', 1, 'password', 'admin');

INSERT INTO user_role (user_id, role_id) VALUES (-100, -100);
INSERT INTO user_role (user_id, role_id) VALUES (-101, -100);
INSERT INTO user_role (user_id, role_id) VALUES (-101, -101);

