CREATE OR REPLACE FUNCTION total_contacts(person1_id int, person2_id int)
    RETURNS int AS
$amount$
DECLARE
    amount       int := 0;
    first_second int;
    second_first int;
BEGIN
    IF person1_id IS NULL OR person2_id IS NULL THEN
        RAISE EXCEPTION 'Person id is null';
    END IF;

    SELECT COUNT(p.id)
    INTO first_second
    FROM contacts c
             JOIN people_contacts pc ON pc.contact_id = c.id
             JOIN people p ON pc.contacted_person_id = p.id
    WHERE c.subject_id = person1_id
      AND p.id = person2_id
    GROUP BY p.id;
    IF NOT FOUND THEN
        first_second := 0;
    END IF;

    SELECT COUNT(p.id)
    INTO second_first
    FROM contacts c
             JOIN people_contacts pc ON pc.contact_id = c.id
             JOIN people p ON pc.contacted_person_id = p.id
    WHERE c.subject_id = person2_id
      AND p.id = person1_id
    GROUP BY p.id;
    IF NOT FOUND THEN
        second_first := 0;
    END IF;

    amount := first_second + second_first;

    RETURN amount;
END;
$amount$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_total_contacts() RETURNS trigger AS
$update_total_contacts$
DECLARE
    subject_id   int;
    contacted_id int;
BEGIN
    NEW.total_contacts := NULL;

    IF NEW.contact_id IS NULL THEN
        RAISE EXCEPTION 'Contact id is null';
    END IF;
    IF NEW.contacted_person_id IS NULL THEN
        RAISE EXCEPTION 'Contacted person id is null';
    END IF;

    SELECT c.subject_id INTO subject_id FROM contacts c WHERE c.id = NEW.contact_id;
    SELECT p.id INTO contacted_id FROM people p WHERE p.id = NEW.contacted_person_id;

    NEW.total_contacts := total_contacts(subject_id, contacted_id) + 1;
    RETURN NEW;
END;
$update_total_contacts$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_total_contacts_trigger
    BEFORE INSERT OR UPDATE
    ON people_contacts
    FOR EACH ROW
EXECUTE FUNCTION update_total_contacts();