package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
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
import java.io.InputStreamReader;
import java.util.List;

public class AddCommand extends Command {
    private final CollectionManager collectionManager;
    private BufferedReader reader;

    public AddCommand(final CollectionManager collectionManager) {
        super("add", "adds new element to collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }

        reader = new BufferedReader(new InputStreamReader(System.in));
        Dragon dragon;
        if (System.in instanceof FileInputStream) {
            try {
                dragon = parseDragonFromFile();
            } catch (IllegalArgumentException | IOException e) {
                throw new InvalidScriptException(e.getMessage());
            }
        } else {
            dragon = parseDragonFromConsole();
        }
        collectionManager.add(dragon);
    }

    private Dragon parseDragonFromConsole() {
        throw new UnsupportedOperationException();
    }

    private Dragon parseDragonFromFile() throws IOException {
        var name = reader.readLine();
        var coordinates = parseCoordinatesFromFile();
        var age = Long.parseLong(reader.readLine());
        var color = Color.valueOf(reader.readLine());
        var type = DragonType.valueOf(reader.readLine());
        var character = DragonCharacter.valueOf(reader.readLine());
        var head = parseHeadFromFile();

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

    private Coordinates parseCoordinatesFromFile() throws IOException {
        var x = reader.readLine();
        var y = reader.readLine();
        try {
            return new Coordinates(Float.parseFloat(x), Float.parseFloat(y));
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid coordinates format");
        }
    }

    private DragonHead parseHeadFromFile() throws IOException {
        var eyesCount = reader.readLine();
        return new DragonHead(Double.parseDouble(eyesCount));
    }
}
