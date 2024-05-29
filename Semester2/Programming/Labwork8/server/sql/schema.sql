BEGIN;

CREATE TYPE color AS ENUM ('GREEN', 'ORANGE', 'BROWN');
CREATE TYPE dtype AS ENUM ('WATER', 'UNDERGROUND', 'AIR', 'FIRE');
CREATE TYPE dcharacter AS ENUM ('EVIL', 'CHAOTIC', 'FICKLE');

CREATE TABLE IF NOT EXISTS users
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(40) UNIQUE NOT NULL,
    password_digest VARCHAR(64)        NOT NULL,
    salt            VARCHAR(10)        NOT NULL
);

CREATE TABLE IF NOT EXISTS dragon_heads
(
    id         SERIAL PRIMARY KEY,
    eye_count  DOUBLE PRECISION NOT NULL,
    creator_id INT              NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dragons
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(40) NOT NULL,
    x             FLOAT       NOT NULL,
    y             FLOAT       NOT NULL,
    creation_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    age           BIGINT      NOT NULL,
    color         color       NOT NULL,
    type          dtype       NOT NULL,
    character     dcharacter  NOT NULL,
    head_id       INT         REFERENCES dragon_heads (id) ON DELETE SET NULL,
    creator_id    INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

END;