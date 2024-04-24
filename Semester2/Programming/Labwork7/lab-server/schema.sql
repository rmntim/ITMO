CREATE TABLE IF NOT EXISTS dragons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    age BIGINT NOT NULL,
    color VARCHAR(255) NOT NULL CHECK (color IN ('GREEN', 'ORANGE', 'BROWN')),
    dragon_type VARCHAR(255) NOT NULL CHECK (dragon_type IN ('WATER', 'UNDERGROUND', 'AIR', 'FIRE')),
    dragon_character VARCHAR(255) NOT NULL CHECK (dragon_character IN ('EVIL', 'CHAOTIC', 'FICKLE'))
);

CREATE TABLE IF NOT EXISTS coordinates (
    id SERIAL PRIMARY KEY,
    x REAL NOT NULL,
    y REAL NOT NULL,
    dragon_id INTEGER NOT NULL REFERENCES dragons ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dragon_heads (
    id SERIAL PRIMARY KEY,
    eyes_count DOUBLE PRECISION NOT NULL,
    dragon_id INTEGER NOT NULL REFERENCES dragons ON DELETE CASCADE
);

INSERT INTO dragons (name, creation_date, age, color, dragon_type, dragon_character) VALUES
    ('Drake', '2020-01-01', 10, 'GREEN', 'WATER', 'EVIL');

INSERT INTO coordinates (x, y, dragon_id) VALUES
    (-5.0, 2.0, 1);

INSERT INTO dragon_heads (eyes_count, dragon_id) VALUES
    (3.0, 1);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash BYTEA NOT NULL
);