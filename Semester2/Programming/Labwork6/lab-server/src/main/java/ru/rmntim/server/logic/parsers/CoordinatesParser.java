package ru.rmntim.server.logic.parsers;

import ru.rmntim.server.exceptions.BuildCancelledException;
import ru.rmntim.server.exceptions.ValidationException;
import ru.rmntim.server.logic.ExecutionContext;
import ru.rmntim.server.models.Coordinates;
import ru.rmntim.server.validators.CoordinatesValidator;

import java.io.BufferedReader;
import java.io.IOException;

public class CoordinatesParser implements Parser<Coordinates> {
    private final ExecutionContext context;
    private final CoordinatesValidator validator;

    public CoordinatesParser(final ExecutionContext context) {
        this.context = context;
        this.validator = new CoordinatesValidator();
    }

    @Override
    public Coordinates parse() {
        return parseCoordinates(context.getReader());
    }

    private Coordinates parseCoordinates(final BufferedReader reader) {
        var x = parseX(reader);
        var y = parseY(reader);
        return new Coordinates(x, y);
    }

    private float parseY(final BufferedReader reader) {
        String inputY;

        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter Y coordinate: ");
                }
                inputY = reader.readLine();
                if (inputY == null) {
                    throw new BuildCancelledException();
                }
                var y = Float.parseFloat(inputY);
                validator.validateY(y);
                return y;
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException | ValidationException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }

    private float parseX(final BufferedReader reader) {
        String inputX;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter X coordinate: ");
                }
                inputX = reader.readLine();
                if (inputX == null) {
                    throw new BuildCancelledException();
                }
                var x = Float.parseFloat(inputX);
                validator.validateX(x);
                return x;
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException | ValidationException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }
}
