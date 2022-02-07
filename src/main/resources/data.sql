insert into role (name) values('CONSUMER');
insert into role (name) values('PROVIDER');
insert into role (name) values('ADMINISTRATOR');

insert into tariff_status (name) values('inactive');
insert into tariff_status (name) values('active');
insert into tariff_status (name) values('archive');

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

SELECT DISTINCT t.* FROM transmitter t
    LEFT JOIN tariff_connected_transmitters tct
        ON t.id = tct.connected_transmitters_id AND tct.tariff_id = ?10
    INNER JOIN transmitter_available_addresses taa ON t.id = taa.transmitter_id
    INNER JOIN address a on a.id = taa.available_addresses_id
WHERE tct.connected_transmitters_id IS NULL AND tct.tariff_id IS NULL
  AND a.country LIKE ?1 AND a.region LIKE ?2 AND a.city LIKE ?3 AND a.street LIKE ?4
  AND a.house LIKE ?5 AND a.building LIKE ?6 AND a.flat LIKE ?7
LIMIT ?8 OFFSET ?9;

SELECT count(*) FROM Address
    WHERE flat = ?1 AND building = ?2 AND house = ?3
    AND street = ?4 AND city = ?5 AND region = ?6 AND country = ?7;

SELECT t.* FROM tariff t
    INNER JOIN tariff_connected_transmitters tct
        ON t.id = tct.tariff_id
    INNER JOIN transmitter t2
        ON t2.id = tct.connected_transmitters_id
    INNER JOIN transmitter_status ts
        ON t2.status_id = ts.id
    INNER JOIN transmitter_available_addresses taa
        ON tct.connected_transmitters_id = taa.transmitter_id
    INNER JOIN address a
        ON taa.available_addresses_id = a.id
    INNER JOIN tariff_status s
        ON t.status_id = s.id
WHERE a.id = ?3 AND ts.name = 'turned on' AND s.name = 'active'
LIMIT ?1 OFFSET ?2