package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.exceptions.BuildCancelledException;
import ru.rmntim.cli.exceptions.InvalidScriptException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.models.Color;
import ru.rmntim.cli.models.Coordinates;
import ru.rmntim.cli.models.Dragon;
import ru.rmntim.cli.models.DragonCharacter;
import ru.rmntim.cli.models.DragonHead;
import ru.rmntim.cli.models.DragonType;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AddCommand extends Command {
    private final CollectionManager collectionManager;

    public AddCommand(final CollectionManager collectionManager) {
        super("add", "adds new element to collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, BufferedReader reader) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }

        Dragon dragon;
        if (System.in instanceof FileInputStream) {
            try {
                dragon = parseDragonFromFile(reader);
            } catch (IllegalArgumentException | IOException e) {
                throw new InvalidScriptException(e.getMessage());
            }
        } else {
            dragon = parseDragonFromConsole(reader);
        }
        collectionManager.add(dragon);
    }

    private Dragon parseDragonFromConsole(final BufferedReader reader) {
        var name = askName(reader);
        var coordinates = askCoordinates(reader);
        var age = askAge(reader);
        var color = askColor(reader);
        var type = askType(reader);
        var character = askCharacter(reader);
        var head = askHead(reader);

        return new Dragon(collectionManager.getLastSavedId() + 1, name, coordinates, age, color, type, character, head);
    }

    private DragonHead askHead(final BufferedReader reader) {
        String answer;
        System.out.print("Do you want to add head? [y/n]");
        try {
            answer = reader.readLine();
            if (answer == null) {
                throw new BuildCancelledException();
            }
            if ("n".equalsIgnoreCase(answer)) {
                return null;
            }
        } catch (IOException e) {
            throw new InvalidScriptException(e.getMessage());
        }

        String inputEyesCount;
        for (; ; ) {
            try {
                System.out.print("Enter eyes count: ");
                inputEyesCount = reader.readLine();
                if (inputEyesCount == null) {
                    throw new BuildCancelledException();
                }
                var eyesCount = Double.parseDouble(inputEyesCount);
                return new DragonHead(eyesCount);
            } catch (IOException e) {
                throw new InvalidScriptException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            }
        }
    }

    private DragonCharacter askCharacter(final BufferedReader reader) {
        String inputCharacter;
        for (; ; ) {
            try {
                System.out.print("Enter dragon character (Possible values: "
                        + Arrays.toString(DragonCharacter.values())
                        + "): ");
                inputCharacter = reader.readLine();
                if (inputCharacter == null) {
                    throw new BuildCancelledException();
                }
                return DragonCharacter.valueOf(inputCharacter.toUpperCase());
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid character. Try again.");
            }
        }
    }

    private DragonType askType(final BufferedReader reader) {
        String inputType;
        for (; ; ) {
            try {
                System.out.print("Enter dragon type (Possible values: "
                        + Arrays.toString(DragonType.values())
                        + "): ");
                inputType = reader.readLine();
                if (inputType == null) {
                    throw new BuildCancelledException();
                }
                return DragonType.valueOf(inputType.toUpperCase());
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid type. Try again.");
            }
        }
    }

    private Color askColor(final BufferedReader reader) {
        String inputColor;
        for (; ; ) {
            try {
                System.out.print("Enter color (Possible values: "
                        + Arrays.toString(Color.values())
                        + "): ");
                inputColor = reader.readLine();
                if (inputColor == null) {
                    throw new BuildCancelledException();
                }
                return Color.valueOf(inputColor.toUpperCase());
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid color. Try again.");
            }
        }
    }

    private long askAge(final BufferedReader reader) {
        String inputAge;
        for (; ; ) {
            try {
                System.out.print("Enter age: ");
                inputAge = reader.readLine();
                if (inputAge == null) {
                    throw new BuildCancelledException();
                }
                var age = Long.parseLong(inputAge);
                if (age > 0) {
                    return age;
                }
                System.out.println("Age must be positive");
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid age");
            }
        }
    }

    private Coordinates askCoordinates(final BufferedReader reader) {
        String inputX;
        String inputY;
        for (; ; ) {
            try {
                System.out.print("Enter X coordinate: ");
                inputX = reader.readLine();
                if (inputX == null) {
                    throw new BuildCancelledException();
                }
                var x = Float.parseFloat(inputX);

                System.out.print("Enter Y coordinate: ");
                inputY = reader.readLine();
                if (inputY == null) {
                    throw new BuildCancelledException();
                }
                var y = Float.parseFloat(inputY);

                try {
                    return new Coordinates(x, y);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid coordinates. Try again.");
                }
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private String askName(final BufferedReader reader) {
        String name;
        try {
            for (; ; ) {
                System.out.print("Enter name: ");
                name = reader.readLine();
                if (name == null) {
                    throw new BuildCancelledException();
                }
                if (!name.isBlank()) {
                    return name;
                }
                System.out.println("Name can't be empty");
            }
        } catch (IOException e) {
            throw new BuildCancelledException(e.getMessage());
        }
    }

    private Dragon parseDragonFromFile(final BufferedReader reader) throws IOException {
        var name = reader.readLine();
        var coordinates = parseCoordinatesFromFile(reader);
        var age = Long.parseLong(reader.readLine());
        var color = Color.valueOf(reader.readLine());
        var type = DragonType.valueOf(reader.readLine());
        var character = DragonCharacter.valueOf(reader.readLine());
        var head = parseHeadFromFile(reader);

        return new Dragon(
                collectionManager.getLastSavedId() + 1,
                name,
                coordinates,
                age,
                color,
                type,
                character,
                head
        );
    }

    private Coordinates parseCoordinatesFromFile(final BufferedReader reader) throws IOException {
        var x = reader.readLine();
        var y = reader.readLine();
        try {
            return new Coordinates(Float.parseFloat(x), Float.parseFloat(y));
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid coordinates format");
        }
    }

    private DragonHead parseHeadFromFile(final BufferedReader reader) throws IOException {
        var eyesCount = reader.readLine();
        return new DragonHead(Double.parseDouble(eyesCount));
    }
}
