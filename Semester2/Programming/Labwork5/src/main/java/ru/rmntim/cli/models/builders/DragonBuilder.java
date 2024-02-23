package ru.rmntim.cli.models.builders;

import ru.rmntim.cli.models.Dragon;

public class DragonBuilder implements Builder<Dragon> {
    private final int lastSavedId;

    public DragonBuilder(int lastSavedId) {
        this.lastSavedId = lastSavedId;
    }

    @Override
    public Dragon build() throws InvalidBuildException {
        try {
            return new Dragon(
                    lastSavedId + 1,
                    parseName(),
                    parseCoordinates(),
                    parseCreationDate(),
                    parseAge(),
                    parseColor(),
                    parseType(),
                    parseCharacter(),
                    parseHead()
            );
        } catch (IllegalArgumentException iae) {
            throw new InvalidBuildException();
        }
    }
}
