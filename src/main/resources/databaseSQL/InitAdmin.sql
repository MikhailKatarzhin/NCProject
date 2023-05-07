INSERT INTO "user" (id, email, password, username)
VALUES (0, 'admin@mail.ru', '$2a$10$75VDIHkeuUXM8.IuKJN6yu.dx/7Cj5Zho3zNGEi3Zl0yNi47ATzAe', 'admin');
INSERT INTO personality (id, first_name, last_name, patronymic)
VALUES (0, '', '', '');
INSERT INTO wallet(id, balance)
VALUES (0, 0);
INSERT INTO user_role_set(user_id, role_set_id)
VALUES (0, 3);