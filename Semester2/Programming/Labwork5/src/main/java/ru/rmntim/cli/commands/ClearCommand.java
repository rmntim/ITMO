package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;

import java.io.BufferedReader;
import java.util.List;

public class ClearCommand extends Command {
    private final CollectionManager collectionManager;

    public ClearCommand(final CollectionManager collectionManager) {
        super("clear", "clears collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, final BufferedReader reader) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }
        collectionManager.clear();
    }
}
