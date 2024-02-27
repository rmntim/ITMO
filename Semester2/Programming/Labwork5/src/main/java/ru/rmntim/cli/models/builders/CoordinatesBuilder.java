package ru.rmntim.cli.models.builders;

import ru.rmntim.cli.exceptions.InvalidBuildException;
import ru.rmntim.cli.models.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CoordinatesBuilder {
    private final BufferedReader reader;

    public CoordinatesBuilder(final InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public Coordinates build() throws InvalidBuildException {
        try {
            System.out.print("Enter x value: ");
            var x = Float.parseFloat(reader.readLine());
            System.out.print("Enter y value: ");
            var y = Float.parseFloat(reader.readLine());
            return new Coordinates(
                    x,
                    y
            );
        } catch (IllegalArgumentException | IOException | NullPointerException e) {
            throw new InvalidBuildException();
        }
    }
}
