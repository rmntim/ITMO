package ru.rmntim.cli.models.builders;

import ru.rmntim.cli.exceptions.InvalidBuildException;
import ru.rmntim.cli.models.Color;
import ru.rmntim.cli.models.Coordinates;
import ru.rmntim.cli.models.Dragon;
import ru.rmntim.cli.models.DragonCharacter;
import ru.rmntim.cli.models.DragonHead;
import ru.rmntim.cli.models.DragonType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DragonBuilder {
    private final int lastSavedId;
    private final InputStream inputStream;
    private final BufferedReader reader;

    public DragonBuilder(int lastSavedId, final InputStream inputStream) {
        this.lastSavedId = lastSavedId;
        this.inputStream = inputStream;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public Dragon build() throws InvalidBuildException {
        try {
            return new Dragon(
                    lastSavedId + 1,
                    parseName(),
                    parseCoordinates(),
                    parseAge(),
                    parseColor(),
                    parseType(),
                    parseCharacter(),
                    parseHead()
            );
        } catch (IOException iae) {
            throw new InvalidBuildException();
        }
    }

    private DragonHead parseHead() {
        return null;
    }

    private DragonCharacter parseCharacter() {
        return DragonCharacter.CHAOTIC;
    }

    private DragonType parseType() {
        return DragonType.AIR;
    }

    private Color parseColor() {
        return Color.BROWN;
    }

    private long parseAge() throws IOException, InvalidBuildException {
        System.out.print("Enter age: ");
        try {
            var age = reader.readLine();
            if (age == null) {
                throw new InvalidBuildException();
            }
            return Long.parseLong(age);
        } catch (NumberFormatException e) {
            throw new InvalidBuildException();
        }
    }

    private Coordinates parseCoordinates() throws InvalidBuildException, IOException {
        System.out.println("Enter coordinates: ");
        return new CoordinatesBuilder(inputStream).build();
    }

    private String parseName() throws IOException, InvalidBuildException {
        System.out.print("Enter name: ");
        String name;
        do {
            name = reader.readLine();
            if (name == null) {
                throw new InvalidBuildException();
            }
        } while (name.isBlank());
        return name;
    }
}
