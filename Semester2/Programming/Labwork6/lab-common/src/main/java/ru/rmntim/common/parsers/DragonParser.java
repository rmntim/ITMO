package ru.rmntim.common.parsers;

import ru.rmntim.common.GlobalInput;
import ru.rmntim.common.models.Color;
import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.models.DragonCharacter;
import ru.rmntim.common.models.DragonType;

import java.io.IOException;
import java.util.Arrays;

public final class DragonParser {
    private DragonParser() {
    }

    public static Dragon parse() {
        var name = parseName();
        var coordinates = CoordinatesParser.parse();
        var age = parseAge();
        var color = parseColor();
        var type = parseType();
        var character = parseCharacter();
        var head = DragonHeadParser.parse();

        return new Dragon(name, coordinates, age, color, type, character, head);
    }

    private static DragonCharacter parseCharacter() {
        String inputCharacter;
        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter dragon character (Possible values: "
                            + Arrays.toString(DragonCharacter.values())
                            + "): ");
                }
                inputCharacter = GlobalInput.getReader().readLine();
                if (inputCharacter == null) {
                    throw new BuildCancelledException();
                }
                return DragonCharacter.valueOf(inputCharacter.toUpperCase());
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Bad input: No such character");
            }
        }
    }

    private static DragonType parseType() {
        String inputType;
        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter dragon type (Possible values: "
                            + Arrays.toString(DragonType.values())
                            + "): ");
                }
                inputType = GlobalInput.getReader().readLine();
                if (inputType == null) {
                    throw new BuildCancelledException();
                }
                return DragonType.valueOf(inputType.toUpperCase());
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Bad input: No such type");
            }
        }
    }

    private static Color parseColor() {
        String inputColor;
        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter color (Possible values: "
                            + Arrays.toString(Color.values())
                            + "): ");
                }
                inputColor = GlobalInput.getReader().readLine();
                if (inputColor == null) {
                    throw new BuildCancelledException();
                }
                return Color.valueOf(inputColor.toUpperCase());
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Bad input: No such color");
            }
        }
    }

    private static long parseAge() {
        String inputAge;
        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter age: ");
                }
                inputAge = GlobalInput.getReader().readLine();
                if (inputAge == null) {
                    throw new BuildCancelledException();
                }
                return Long.parseLong(inputAge);
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }

    private static String parseName() {
        String name;
        try {
            if (!GlobalInput.isInFile()) {
                System.out.print("Enter name: ");
            }
            name = GlobalInput.getReader().readLine();
            if (name == null) {
                throw new BuildCancelledException();
            }
            return name;
        } catch (IOException e) {
            throw new BuildCancelledException(e.getMessage());
        }
    }
}
