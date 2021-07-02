----------------------------------------------------------------
-- ROLES
----------------------------------------------------------------
CREATE TABLE roles
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

----------------------------------------------------------------
-- STATUSES
----------------------------------------------------------------
CREATE TABLE statuses
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

----------------------------------------------------------------
-- USERS
----------------------------------------------------------------
CREATE TABLE users
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    login VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    role_id BIGSERIAL NOT NULL REFERENCES roles(id) DEFAULT 1,
    status_id BIGSERIAL NOT NULL REFERENCES statuses(id) DEFAULT 1
);

----------------------------------------------------------------
-- SERVICES
----------------------------------------------------------------
CREATE TABLE services
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

----------------------------------------------------------------
-- TARIFFS
----------------------------------------------------------------
CREATE TABLE tariffs
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(200) NOT NULL,
    duration INTEGER NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    service_id BIGSERIAL NOT NULL REFERENCES services(id) ON DELETE CASCADE
);

----------------------------------------------------------------
-- payments
----------------------------------------------------------------
CREATE TABLE payments
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    payment NUMERIC(19,2) NOT NULL,
    user_id BIGSERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

----------------------------------------------------------------
-- USER SERVICE TARIFF
----------------------------------------------------------------
CREATE TABLE tariff_user
(
    user_id BIGSERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    tariff_id BIGSERIAL NOT NULL REFERENCES tariffs(id) ON DELETE CASCADE,
    date_start TIMESTAMPTZ NOT NULL,
	date_end TIMESTAMPTZ NOT NULL
);

----------------------------------------------------------------
-- TEST DATA
----------------------------------------------------------------
INSERT INTO roles VALUES(DEFAULT, 'USER');
INSERT INTO roles VALUES(DEFAULT, 'ADMIN');

INSERT INTO statuses VALUES(DEFAULT, 'ENABLED');
INSERT INTO statuses VALUES(DEFAULT, 'DISABLED');

INSERT INTO users VALUES(DEFAULT, 'Admin', '21232F297A57A5A743894A0E4A801FC3', 'Mikhail', 'Kutuzov', 2 , 1);
INSERT INTO users VALUES(DEFAULT, 'Yaroslav', 'E2D1D3F22F3782F45F9B2C3C0B013D13', 'Yaroslav', 'Panov', 1 , 1);
INSERT INTO users VALUES(DEFAULT, 'Pasha', 'EC8BC8E2B120D143E7274DE2508F3F6F', 'Pavel', 'Balandin', 1 , 1);
INSERT INTO users VALUES(DEFAULT, 'Dasha', '4BEA249142664D11A289FFDF30225A91', 'Darya', 'Petrenko', 1 , 1);
INSERT INTO users VALUES(DEFAULT, 'Sasha', '481F693417E9A74E783CAEA72063B606', 'Oleksandr', 'Novikov', 1 , 1);
INSERT INTO users VALUES(DEFAULT, 'Masha', 'C3CC6E312D2BAD42CF535AAC3A259ABD', 'Mariya', 'Sidorova', 1 , 1);
INSERT INTO users VALUES(DEFAULT, 'Sergey', 'D947F2DEF6D2F32C2FC7DF910ED00600', 'Sergey', 'Voronin', 1 , 1);

INSERT INTO services VALUES(DEFAULT, 'Телефон');
INSERT INTO services VALUES(DEFAULT, 'Інтернет');
INSERT INTO services VALUES(DEFAULT, 'Кабельне ТБ');
INSERT INTO services VALUES(DEFAULT, 'IP-TV');

INSERT INTO tariffs VALUES (DEFAULT, 'Light', '200 хвилини, 1000 мб', 30, 50, 1);
INSERT INTO tariffs VALUES (DEFAULT, 'Light +', '300 хвилини, 1500 мб', 30, 75, 1);
INSERT INTO tariffs VALUES (DEFAULT, 'Standard', '400 хвилини, 2000 мб', 30, 100, 1);
INSERT INTO tariffs VALUES (DEFAULT, 'Ultimate', 'Безліміт на всіх операторів, 4000 мб', 30, 200, 1);
INSERT INTO tariffs VALUES (DEFAULT, 'Ultimate Pro', 'Безліміт на всіх операторів, Безліміт інтернет', 30, 500, 1);

INSERT INTO tariffs VALUES (DEFAULT, 'Start', 'до 50 Мбіт/с', 30, 100, 2);
INSERT INTO tariffs VALUES (DEFAULT, 'Ultra', 'до 100 Мбіт/с', 30, 200, 2);
INSERT INTO tariffs VALUES (DEFAULT, 'Mega','до 1 Гбіт/с', 30, 399, 2);

INSERT INTO tariffs VALUES (DEFAULT, 'Start TV', '140 каналів', 30, 255, 3);
INSERT INTO tariffs VALUES (DEFAULT, 'VIP TV', '200 каналів', 30, 320, 3);

INSERT INTO tariffs VALUES (DEFAULT, 'VIP', 'до 100 Мбіт/с, 140 каналів', 60, 400, 4);


INSERT INTO tariff_user VALUES(2,1, current_date, current_date + interval '30 days');
INSERT INTO tariff_user VALUES(2,6, current_date, current_date + interval '30 days');
INSERT INTO tariff_user VALUES(3,7, current_date, current_date + interval '30 days');
INSERT INTO tariff_user VALUES(4,10, current_date, current_date + interval '30 days');
INSERT INTO tariff_user VALUES(4,11, current_date, current_date + interval '30 days');

INSERT INTO payments VALUES(DEFAULT, 400, 2, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 100, 2, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 200, 2, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 2000, 3, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 400, 3, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 100, 3, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 200, 3, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 200, 4, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 200, 4, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 200, 4, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 600, 5, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 600, 5, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 600, 5, DEFAULT);
INSERT INTO payments VALUES(DEFAULT, 600, 5, DEFAULT);

CREATE OR REPLACE FUNCTION make_order(varchar(30), int[])
RETURNS numeric AS
$$
DECLARE
current_user_id int; -- current user id
psum numeric; -- user payments sum
tsum numeric; -- tariffs sum
tariff tariffs%rowtype; -- tariff
BEGIN
SELECT u.id into current_user_id FROM users u where u.login = $1;
SELECT sum(payment) into psum FROM payments p where p.user_id = current_user_id;
SELECT sum(price) into tsum FROM tariffs t where t.id = ANY($2);
IF (SELECT ARRAY(SELECT ut.tariff_id FROM user_tariff ut WHERE user_id = current_user_id)) && $2 THEN
	raise exception 'User have already had the tariffs';
END IF;
IF psum - tsum >= 0 THEN
  FOR tariff IN
    SELECT * FROM tariffs t where t.id = ANY($2)
  LOOP
    INSERT INTO user_tariff VALUES(current_user_id, tariff.id, current_date, current_date + (tariff.duration * interval '1 day'));
  END LOOP;
  INSERT INTO payments VALUES(DEFAULT, -tsum, current_user_id, DEFAULT);
END IF;
RETURN psum - tsum;
END;
$$
LANGUAGE 'plpgsql'