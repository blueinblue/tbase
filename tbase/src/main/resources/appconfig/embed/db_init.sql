select 1 from INFORMATION_SCHEMA.SYSTEM_USERS;

DELETE FROM user_role;
DELETE FROM role;
DELETE FROM user;

INSERT INTO role (id, created_by, created_on, object_id, version, name) VALUES (-100, null, SYSDATE, 'ROLE-OBJ-100', 1, 'ADMIN');
INSERT INTO role (id, created_by, created_on, object_id, version, name) VALUES (-101, null, SYSDATE, 'ROLE-OBJ-101', 1, 'USER');
INSERT INTO role (id, created_by, created_on, object_id, version, name) VALUES (-102, null, SYSDATE, 'ROLE-OBJ-102', 1, 'API');

INSERT INTO user (id, created_by, created_on, object_id, version, email, enabled, password, display_name) VALUES (-100, null, SYSDATE, 'USER-OBJ-100', 1, 'user@mail.com', 1, 'password', 'reg_user');
INSERT INTO user (id, created_by, created_on, object_id, version, email, enabled, password, display_name) VALUES (-101, null, SYSDATE, 'USER-OBJ-101', 1, 'admin@mail.com', 1, 'password', 'admin');
INSERT INTO user (id, created_by, created_on, object_id, version, email, enabled, password, display_name) VALUES (-102, null, SYSDATE, 'USER-OBJ-102', 1, 'api@mail.com', 1, 'password', 'api_user');

-- ROLE ASSIGNMENTS
-- Admin User
INSERT INTO user_role (user_id, role_id) VALUES (-101, -100);
INSERT INTO user_role (user_id, role_id) VALUES (-101, -101);

-- API User
INSERT INTO user_role (user_id, role_id) VALUES (-102, -102);

-- Reg User
INSERT INTO user_role (user_id, role_id) VALUES (-100, -101);

-- ACCOUNT NOTES
-- Reg User
INSERT INTO user_note(id, created_by, created_on, object_id, version, text, user_id) VALUES (-100, -101, SYSDATE, 'USER-NOTE-OBJ-100', 1, 'Do not call at home because he sleeps all day.', -100);
INSERT INTO user_note(id, created_by, created_on, object_id, version, text, user_id) VALUES (-101, -101, SYSDATE, 'USER-NOTE-OBJ-101', 1, 'At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.', -100);