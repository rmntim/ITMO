CREATE TABLE IF NOT EXISTS dragons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    age BIGINT NOT NULL,
    color VARCHAR(255) NOT NULL CHECK (color IN ('GREEN', 'ORANGE', 'BROWN')),
    dragon_type VARCHAR(255) NOT NULL CHECK (dragon_type IN ('WATER', 'UNDERGROUND', 'AIR', 'FIRE')),
    dragon_character VARCHAR(255) NOT NULL CHECK (dragon_character IN ('EVIL', 'CHAOS', 'FICKLE'))
);

CREATE TABLE IF NOT EXISTS coordinates (
    id SERIAL PRIMARY KEY,
    x REAL NOT NULL,
    y REAL NOT NULL,
    dragon_id INTEGER NOT NULL REFERENCES dragons
);

CREATE TABLE IF NOT EXISTS dragon_heads (
    id SERIAL PRIMARY KEY,
    eyes_count DOUBLE PRECISION NOT NULL,
    dragon_id INTEGER NOT NULL REFERENCES dragons
);