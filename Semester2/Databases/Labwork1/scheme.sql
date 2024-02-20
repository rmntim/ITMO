BEGIN;

CREATE TYPE sex AS ENUM ('М', 'Ж');
CREATE TYPE contact_reason AS ENUM (
  'возбудить интерес'
);

CREATE TABLE people (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  sex sex NOT NULL
);

CREATE TABLE council_members (
  id SERIAL PRIMARY KEY,
  person_id INT NOT NULL REFERENCES people
);

CREATE TABLE apprentices (
  id SERIAL PRIMARY KEY,
  person_id INT NOT NULL REFERENCES people,
  master_id INT NOT NULL REFERENCES council_members
);

CREATE TABLE contacts (
  id SERIAL PRIMARY KEY,
  reason contact_reason NOT NULL,
  made_at TIMESTAMP NOT NULL DEFAULT NOW(),
  subject_id INT NOT NULL REFERENCES people
);

CREATE TABLE people_contacts (
  contacted_person_id  INT NOT NULL REFERENCES people,
  contact_id INT NOT NULL REFERENCES contacts,
  CONSTRAINT people_contact_id PRIMARY KEY (contacted_person_id, contact_id)
);

INSERT INTO people (name, sex)
VALUES ('Джизирак', 'М'),
       ('Олвин',    'М'),
       ('Колега 1', 'М'),
       ('Колега 2', 'Ж'),
       ('Колега 3', 'М');

INSERT INTO council_members (person_id)
VALUES (3), (4), (5);

INSERT INTO apprentices (person_id, master_id)
VALUES (2, 1);

INSERT INTO contacts (reason, subject_id)
VALUES ('возбудить интерес', 1);

INSERT INTO people_contacts (contacted_person_id, contact_id)
VALUES (3, 1), (4, 1), (5, 1);

COMMIT;
