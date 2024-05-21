package common.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

public record Dragon(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age,
                     Color color, DragonType type, DragonCharacter character,
                     DragonHead head) implements Comparable<Dragon>, Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Override
    public int compareTo(Dragon o) {
        return this.id - o.id();
    }

    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (age == null || age <= 0) return false;
        if (color == null) return false;
        if (type == null) return false;
        return character != null;
    }
}
