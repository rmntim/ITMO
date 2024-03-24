package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;

import java.util.List;

/**
 * Clear the collection
 */
public class ClearCommand extends Command {
    private final CollectionManager collectionManager;

    public ClearCommand(final CollectionManager collectionManager) {
        super("clear", "clears collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }
        collectionManager.clear();
    }
}
