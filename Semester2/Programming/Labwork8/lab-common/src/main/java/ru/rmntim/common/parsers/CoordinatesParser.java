package ru.rmntim.common.parsers;

import ru.rmntim.common.GlobalInput;
import ru.rmntim.common.models.Coordinates;

import java.io.IOException;

public final class CoordinatesParser {
    private CoordinatesParser() {
    }

    /**
     * Creates new {@link Coordinates} object from stdin or file.
     *
     * @return parsed coordinates
     * @throws BuildCancelledException if EOF is reached
     */
    public static Coordinates parse() {
        return parseCoordinates();
    }

    private static Coordinates parseCoordinates() {
        var x = parseX();
        var y = parseY();
        return new Coordinates(x, y);
    }

    private static float parseY() {
        String inputY;

        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter Y coordinate: ");
                }
                inputY = GlobalInput.getReader().readLine();
                if (inputY == null) {
                    throw new BuildCancelledException();
                }
                return Float.parseFloat(inputY);
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }

    private static float parseX() {
        String inputX;
        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter X coordinate: ");
                }
                inputX = GlobalInput.getReader().readLine();
                if (inputX == null) {
                    throw new BuildCancelledException();
                }
                return Float.parseFloat(inputX);
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }
}
