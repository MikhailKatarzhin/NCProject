CREATE TABLE IF NOT EXISTS "user"(
    id BIGSERIAL NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    CONSTRAINT "PK_User_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_Username" UNIQUE (username),
    CONSTRAINT "UQ_User_Email" UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS personality(
    id BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL ,
    last_name VARCHAR(255) NOT NULL ,
    patronymic VARCHAR(255) NOT NULL ,
    CONSTRAINT "PK_Personality_Id" PRIMARY KEY (id),
    CONSTRAINT "FK__Personality_belongs_User" FOREIGN KEY (id)
        REFERENCES "user" (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS wallet(
    id BIGINT NOT NULL,
    balance BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT "PK_Wallet_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Wallet_belongs_User" FOREIGN KEY (id)
        REFERENCES "user" (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS role(
    id BIGSERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT "PK_Role_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_Role_Name" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS user_role_set(
    user_id BIGINT NOT NULL,
    role_set_id BIGINT NOT NULL,
    CONSTRAINT "PK_User_role_set_Id" PRIMARY KEY (user_id, role_set_id),
    CONSTRAINT "FK_User_has_Role" FOREIGN KEY (role_set_id)
        REFERENCES role (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "FK_Role_belongs_User" FOREIGN KEY (user_id)
        REFERENCES "user"(id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS tariff_status(
    id BIGSERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT "PK_Tariff_status_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_Tariff_status_Name" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS tariff(
    id BIGSERIAL NOT NULL,
    description VARCHAR(255) NOT NULL DEFAULT '',
    price BIGINT NOT NULL DEFAULT 0,
    title VARCHAR(255) NOT NULL DEFAULT '',
    provider_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT "PK_Tariff_Id" PRIMARY KEY (id),
    CONSTRAINT "CH_Price_Not_negative" CHECK ( price >= 0 ),
    CONSTRAINT "FK_Tariff_has_Tariff_status" FOREIGN KEY (status_id)
        REFERENCES tariff_status (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "FK_Tariff_belongs_Provider" FOREIGN KEY (provider_id)
        REFERENCES "user"(id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS address
(
    id BIGSERIAL NOT NULL,
    country VARCHAR(255) NOT NULL DEFAULT '',
    region VARCHAR(255) NOT NULL DEFAULT '',
    city VARCHAR(255) NOT NULL DEFAULT '',
    street VARCHAR(255) NOT NULL DEFAULT '',
    house BIGINT NOT NULL DEFAULT 1,
    building BIGINT NOT NULL DEFAULT 1,
    flat BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT "PK_Address" PRIMARY KEY (id),
    CONSTRAINT "CH_Address_House_positive" CHECK ( house > 0 ),
    CONSTRAINT "CH_Address_Building_positive" CHECK ( building > 0 ),
    CONSTRAINT "CH_Address_Flat_positive" CHECK ( flat > 0 )
);

CREATE TABLE IF NOT EXISTS transmitter_status(
    id BIGSERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT "PK_Transmitter_status_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_Transmitter_status_Name" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS transmitter(
     id BIGSERIAL NOT NULL,
     description VARCHAR(255) NOT NULL DEFAULT '',
     address_id BIGINT,
     status_id BIGINT NOT NULL DEFAULT 1,
     CONSTRAINT "PK_Transmitter_Id" PRIMARY KEY (id),
     CONSTRAINT "FK_Transmitter_has_Transmitter_status" FOREIGN KEY (status_id)
        REFERENCES transmitter_status (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
     CONSTRAINT "FK_Transmitter_has_Address" FOREIGN KEY (address_id)
        REFERENCES address (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS transmitter_available_addresses(
    transmitter_id BIGINT NOT NULL,
    available_addresses_id BIGINT NOT NULL,
    CONSTRAINT "PK_Transmitter_available_addresses_Id" PRIMARY KEY (transmitter_id, available_addresses_id),
    CONSTRAINT "FK_Transmitter_cover_Address" FOREIGN KEY (available_addresses_id)
        REFERENCES address (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "FK_Address_covered_by_Transmitter" FOREIGN KEY (transmitter_id)
        REFERENCES transmitter (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS tariff_connected_transmitters(
    tariff_id BIGINT NOT NULL,
    connected_transmitters_id BIGINT NOT NULL,
    CONSTRAINT "PK_Tariff_connected_transmitters_Id" PRIMARY KEY (tariff_id, connected_transmitters_id),
    CONSTRAINT "FK_Transmitter_provide_Tariff" FOREIGN KEY (tariff_id)
        REFERENCES tariff (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "FK_Tariff_occupies_Transmitter" FOREIGN KEY (connected_transmitters_id)
        REFERENCES transmitter (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS contract(
    id BIGSERIAL NOT NULL,
    contract_expiration_date date default current_date,
    description VARCHAR(255) NOT NULL DEFAULT '',
    title VARCHAR(255) NOT NULL DEFAULT '',
    address_id BIGINT NOT NULL,
    consumer_id BIGINT NOT NULL,
    tariff_id BIGINT NOT NULL,
    CONSTRAINT "PK_Contract_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Contract_has_consumer_User" FOREIGN KEY (consumer_id)
        REFERENCES "user"(id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "FK_Contract_belongs_Address" FOREIGN KEY (address_id)
        REFERENCES address (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "FK_Contract_belongs_Tariff" FOREIGN KEY (tariff_id)
        REFERENCES tariff (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);