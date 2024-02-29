package ru.rmntim.cli.logic.parsers;

import ru.rmntim.cli.exceptions.BuildCancelledException;
import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.models.DragonHead;
import ru.rmntim.cli.validators.DragonHeadValidator;

import java.io.BufferedReader;
import java.io.IOException;

public class DragonHeadParser {
    private final ExecutionContext context;
    private final DragonHeadValidator validator;

    public DragonHeadParser(final ExecutionContext context) {
        this.context = context;
        this.validator = new DragonHeadValidator();
    }

    public DragonHead parse() {
        return askDragonHead(context.getReader());
    }

    private DragonHead askDragonHead(final BufferedReader reader) {
        String answer;
        if (context.isInteractive()) {
            System.out.print("Do you want to add head? [y/n] ");
        }
        while (true) {
            try {
                answer = reader.readLine();
                if (answer == null) {
                    throw new BuildCancelledException();
                }
                if ("n".equalsIgnoreCase(answer)) {
                    return null;
                } else if ("y".equalsIgnoreCase(answer)) {
                    break;
                }
                if (context.isInteractive()) {
                    System.out.print("Please enter y or n: ");
                }
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            }
        }

        return parseDragonHead(reader);
    }

    private DragonHead parseDragonHead(BufferedReader reader) {
        String inputEyesCount;
        while (true) {
            try {
                if (context.isInteractive()) {
                    System.out.print("Enter eyes count: ");
                }
                inputEyesCount = reader.readLine();
                if (inputEyesCount == null) {
                    throw new BuildCancelledException();
                }
                var eyesCount = Double.parseDouble(inputEyesCount);
                validator.validateEyesCount(eyesCount);
                return new DragonHead(eyesCount);
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException | ValidationException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }
}
