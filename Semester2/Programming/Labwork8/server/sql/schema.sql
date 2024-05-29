BEGIN;

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
    color         VARCHAR(15) NOT NULL CHECK (color IN ('GREEN', 'ORANGE', 'BROWN')),
    type          VARCHAR(15) NOT NULL CHECK (type IN ('WATER', 'UNDERGROUND', 'AIR', 'FIRE')),
    character     VARCHAR(15) NOT NULL CHECK (character IN ('EVIL', 'CHAOTIC', 'FICKLE')),
    head_id       INT         REFERENCES dragon_heads (id) ON DELETE SET NULL,
    creator_id    INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

END;