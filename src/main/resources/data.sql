insert into role (name) values('CONSUMER');
insert into role (name) values('PROVIDER');
insert into role (name) values('ADMINISTRATOR');

insert into tariff_status (name) values('archive');
insert into tariff_status (name) values('common');

insert into transmitter_status (name) values('turned off');
insert into transmitter_status (name) values('turned on');
insert into transmitter_status (name) values('damaged');
insert into transmitter_status (name) values('being repaired');

INSERT INTO personality (id, first_name, last_name, patronymic) VALUES (1,'','','');
INSERT INTO user (email, password, username)
VALUES ('maikl.1997@mail.ru', '$2a$10$O9gJ0LzfKTwnV.udAG3oHOqtE5YXElvz.aSDKOFY39Jfc9ls5DfqW', 'admin');
INSERT INTO user_role_set(user_id, role_set_id) VALUES (1, 3);

INSERT INTO personality (id,first_name, last_name, patronymic) VALUES (2,'Тестер','Тестеров','Тестирович');
INSERT INTO user (email, password, username)
VALUES ('tester@mail.ru', '$2a$10$O9gJ0LzfKTwnV.udAG3oHOqtE5YXElvz.aSDKOFY39Jfc9ls5DfqW', 'tester');
INSERT INTO user_role_set(user_id, role_set_id) VALUES (2, 1);
INSERT INTO user_role_set(user_id, role_set_id) VALUES (2, 2);

INSERT INTO address(id, building, city, country, flat, house, region, street) VALUES (1, 1, 'test', 'test', 1, 1, 'test', 'test');