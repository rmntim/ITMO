package ru.rmntim.cli.logic.parsers;

import ru.rmntim.cli.exceptions.BuildCancelledException;
import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.models.Color;
import ru.rmntim.cli.models.Dragon;
import ru.rmntim.cli.models.DragonCharacter;
import ru.rmntim.cli.models.DragonType;
import ru.rmntim.cli.validators.DragonValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class for parsing {@link Dragon} from {@code context} reader
 */
public class DragonParser implements Parser<Dragon> {
    private final ExecutionContext context;
    private final DragonValidator validator;
    private final int id;

    public DragonParser(final ExecutionContext context, int id) {
        this.context = context;
        this.validator = new DragonValidator();
        this.id = id;
    }

    @Override
    public Dragon parse() {
        return parseDragon(context.getReader());
    }

    private Dragon parseDragon(final BufferedReader reader) {
        var name = parseName(reader);
        var coordinates = new CoordinatesParser(context).parse();
        var age = parseAge(reader);
        var color = parseColor(reader);
        var type = parseType(reader);
        var character = parseCharacter(reader);
        var head = new DragonHeadParser(context).parse();

        return new Dragon(id, name, coordinates, age, color, type, character, head);
    }

    private DragonCharacter parseCharacter(final BufferedReader reader) {
        String inputCharacter;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter dragon character (Possible values: "
                            + Arrays.toString(DragonCharacter.values())
                            + "): ");
                }
                inputCharacter = reader.readLine();
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

    private DragonType parseType(final BufferedReader reader) {
        String inputType;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter dragon type (Possible values: "
                            + Arrays.toString(DragonType.values())
                            + "): ");
                }
                inputType = reader.readLine();
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

    private Color parseColor(final BufferedReader reader) {
        String inputColor;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter color (Possible values: "
                            + Arrays.toString(Color.values())
                            + "): ");
                }
                inputColor = reader.readLine();
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

    private long parseAge(final BufferedReader reader) {
        String inputAge;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter age: ");
                }
                inputAge = reader.readLine();
                if (inputAge == null) {
                    throw new BuildCancelledException();
                }
                var age = Long.parseLong(inputAge);
                validator.validateAge(age);
                return age;
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException | ValidationException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }

    private String parseName(final BufferedReader reader) {
        String name;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter name: ");
                }
                name = reader.readLine();
                if (name == null) {
                    throw new BuildCancelledException();
                }
                validator.validateName(name);
                return name;
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (ValidationException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }

}
