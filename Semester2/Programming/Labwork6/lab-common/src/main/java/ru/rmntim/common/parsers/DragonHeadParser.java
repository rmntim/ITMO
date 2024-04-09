package ru.rmntim.common.parsers;

import ru.rmntim.common.GlobalInput;
import ru.rmntim.common.models.DragonHead;

import java.io.IOException;

public final class DragonHeadParser {
    private DragonHeadParser() {
    }

    public static DragonHead parse() {
        return askDragonHead();
    }

    private static DragonHead askDragonHead() {
        var reader = GlobalInput.getReader();
        String answer;
        if (!GlobalInput.isInFile()) {
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
                if (!GlobalInput.isInFile()) {
                    System.out.print("Please enter y or n: ");
                }
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            }
        }

        return parseDragonHead();
    }

    private static DragonHead parseDragonHead() {
        String inputEyesCount;
        while (true) {
            try {
                if (!GlobalInput.isInFile()) {
                    System.out.print("Enter eyes count: ");
                }
                inputEyesCount = GlobalInput.getReader().readLine();
                if (inputEyesCount == null) {
                    throw new BuildCancelledException();
                }
                var eyesCount = Double.parseDouble(inputEyesCount);
                return new DragonHead(eyesCount);
            } catch (IOException e) {
                throw new BuildCancelledException(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Bad input: " + e.getMessage());
            }
        }
    }
}
