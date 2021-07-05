----------------------------------------------------------------
-- ROLES
----------------------------------------------------------------
CREATE TABLE roles
(
    id   BIGSERIAL   NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

----------------------------------------------------------------
-- STATUSES
----------------------------------------------------------------
CREATE TABLE statuses
(
    id   BIGSERIAL   NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

----------------------------------------------------------------
-- USERS
----------------------------------------------------------------
CREATE TABLE users
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    login      VARCHAR(30)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(30)  NOT NULL,
    last_name  VARCHAR(30)  NOT NULL,
    status_id  BIGINT       NOT NULL REFERENCES statuses (id) DEFAULT 1
);

----------------------------------------------------------------
-- ROLES_USERS
----------------------------------------------------------------
CREATE TABLE roles_users
(
    role_id BIGINT NOT NULL REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

----------------------------------------------------------------
-- SERVICES
----------------------------------------------------------------
CREATE TABLE services
(
    id   BIGSERIAL   NOT NULL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

----------------------------------------------------------------
-- TARIFFS
----------------------------------------------------------------
CREATE TABLE tariffs
(
    id          BIGSERIAL      NOT NULL PRIMARY KEY,
    name        VARCHAR(30)    NOT NULL,
    description VARCHAR(255)   NOT NULL,
    duration    INTEGER        NOT NULL,
    price       NUMERIC(19, 2) NOT NULL,
    service_id  BIGINT         NOT NULL REFERENCES services (id) ON DELETE CASCADE
);

----------------------------------------------------------------
-- payments
----------------------------------------------------------------
CREATE TABLE payments
(
    id         BIGSERIAL      NOT NULL PRIMARY KEY,
    payment    NUMERIC(19, 2) NOT NULL,
    user_id    BIGINT         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

----------------------------------------------------------------
-- USER SERVICE TARIFF
----------------------------------------------------------------
CREATE TABLE tariff_user
(
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    tariff_id  BIGINT      NOT NULL REFERENCES tariffs (id) ON DELETE CASCADE,
    date_start TIMESTAMPTZ NOT NULL,
    date_end   TIMESTAMPTZ NOT NULL
);

----------------------------------------------------------------
-- TEST DATA
----------------------------------------------------------------
INSERT INTO roles
VALUES (DEFAULT, 'ROLE_USER');
INSERT INTO roles
VALUES (DEFAULT, 'ROLE_ADMIN');

INSERT INTO statuses
VALUES (DEFAULT, 'ENABLED');
INSERT INTO statuses
VALUES (DEFAULT, 'DISABLED');

INSERT INTO users
VALUES (DEFAULT, 'Admin', '$2a$10$UcDsdyEHTL7oweJo4NYGxeEDmvIpvFlDlb.gI97s8HXnrXfhyEJKu', 'Mikhail', 'Kutuzov', 1);
INSERT INTO users
VALUES (DEFAULT, 'Yaroslav', '$2a$10$mdfh2SNZc9uYxSUWQ6vdjOjEAqHuJ5/JTANOdgCtanKDhqvFyELmu', 'Yaroslav', 'Panov', 1);
INSERT INTO users
VALUES (DEFAULT, 'Pasha', '$2a$10$2A4ckiMJhVnLo0/RLHu09unt4CQTicV/R.bimjzODEyzD/TBk0oEC', 'Pavel', 'Balandin', 1);
INSERT INTO users
VALUES (DEFAULT, 'Dasha', '$2a$10$zqn2j2G.u.R8Q4qOF52Nieoi7o4sF2wcbLq05cVZ8Wi5vT8CAXKJO', 'Darya', 'Petrenko', 1);
INSERT INTO users
VALUES (DEFAULT, 'Sergey', '$2a$10$klf/Zs4TX118zE1SEwxByuN0yhZHet9JtoMUf9GUlOYKoYmQAEp..', 'Sergey', 'Voronin', 1);

INSERT INTO roles_users
VALUES (2, 1);
INSERT INTO roles_users
VALUES (1, 2);
INSERT INTO roles_users
VALUES (1, 3);
INSERT INTO roles_users
VALUES (1, 4);
INSERT INTO roles_users
VALUES (1, 5);

INSERT INTO services
VALUES (DEFAULT, 'Телефон');
INSERT INTO services
VALUES (DEFAULT, 'Інтернет');
INSERT INTO services
VALUES (DEFAULT, 'Кабельне ТБ');
INSERT INTO services
VALUES (DEFAULT, 'IP-TV');

INSERT INTO tariffs
VALUES (DEFAULT, 'Light', '200 хвилини, 1000 мб', 30, 50, 1);
INSERT INTO tariffs
VALUES (DEFAULT, 'Light +', '300 хвилини, 1500 мб', 30, 75, 1);
INSERT INTO tariffs
VALUES (DEFAULT, 'Standard', '400 хвилини, 2000 мб', 30, 100, 1);
INSERT INTO tariffs
VALUES (DEFAULT, 'Ultimate', 'Безліміт на всіх операторів, 4000 мб', 30, 200, 1);
INSERT INTO tariffs
VALUES (DEFAULT, 'Ultimate Pro', 'Безліміт на всіх операторів, Безліміт інтернет', 30, 500, 1);

INSERT INTO tariffs
VALUES (DEFAULT, 'Start', 'до 50 Мбіт/с', 30, 100, 2);
INSERT INTO tariffs
VALUES (DEFAULT, 'Ultra', 'до 100 Мбіт/с', 30, 200, 2);
INSERT INTO tariffs
VALUES (DEFAULT, 'Mega', 'до 1 Гбіт/с', 30, 399, 2);

INSERT INTO tariffs
VALUES (DEFAULT, 'Start TV', '140 каналів', 30, 255, 3);
INSERT INTO tariffs
VALUES (DEFAULT, 'VIP TV', '200 каналів', 30, 320, 3);

INSERT INTO tariffs
VALUES (DEFAULT, 'VIP', 'до 100 Мбіт/с, 140 каналів', 60, 400, 4);


INSERT INTO tariff_user
VALUES (2, 1, current_date, current_date + interval '30 days');
INSERT INTO tariff_user
VALUES (2, 6, current_date, current_date + interval '30 days');
INSERT INTO tariff_user
VALUES (3, 7, current_date, current_date + interval '30 days');
INSERT INTO tariff_user
VALUES (4, 10, current_date, current_date + interval '30 days');
INSERT INTO tariff_user
VALUES (4, 11, current_date, current_date + interval '30 days');

INSERT INTO payments
VALUES (DEFAULT, 400, 2, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 100, 2, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 200, 2, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 2000, 3, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 400, 3, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 100, 3, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 200, 3, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 200, 4, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 200, 4, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 200, 4, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 600, 5, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 600, 5, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 600, 5, DEFAULT);
INSERT INTO payments
VALUES (DEFAULT, 600, 5, DEFAULT);