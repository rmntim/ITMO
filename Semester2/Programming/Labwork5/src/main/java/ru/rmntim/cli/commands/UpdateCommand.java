package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.DragonBuilder;

import java.io.BufferedReader;
import java.util.List;

public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(final CollectionManager collectionManager) {
        super("update", "updates element with specified id", List.of("id"));
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, final BufferedReader reader) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " requires one argument");
        }

        try {
            var id = Integer.parseInt(arguments.get(0));
            collectionManager.update(id, () -> DragonBuilder.build(id, reader));
        } catch (NumberFormatException e) {
            throw new BadCommandArgumentsException(arguments.get(0) + " is not a number");
        } catch (IllegalArgumentException e) {
            throw new BadCommandArgumentsException(e.getMessage());
        }
    }
}
