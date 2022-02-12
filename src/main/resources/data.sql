insert into role (name)
values ('CONSUMER'), ('PROVIDER'), ('ADMINISTRATOR');

insert into tariff_status (name)
values ('inactive'), ('active'), ('archive');

insert into transmitter_status (name)
values ('turned off'), ('turned on'), ('damaged'), ('being repaired');

INSERT INTO personality (id, first_name, last_name, patronymic)
VALUES (1, '', '', '');
INSERT INTO wallet(id, balance)
VALUES (1, 0);
INSERT INTO user (id, email, password, username, wallet_id)
VALUES (1, 'admin@mail.ru', '$2a$10$O9gJ0LzfKTwnV.udAG3oHOqtE5YXElvz.aSDKOFY39Jfc9ls5DfqW', 'admin', 1);
INSERT INTO user_role_set(user_id, role_set_id)
VALUES (1, 3);