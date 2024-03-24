package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;

import java.util.List;

/**
 * Removes item with specified id from collection
 */
public class RemoveCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveCommand(final CollectionManager collectionManager) {
        super("remove", "remove item from collection", List.of("id"));
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " requires one argument");
        }

        try {
            var id = Integer.parseInt(arguments.get(0));
            collectionManager.remove(id);
        } catch (NumberFormatException e) {
            throw new BadCommandArgumentsException("Argument must be a number");
        } catch (IllegalArgumentException e) {
            throw new BadCommandArgumentsException("No such item in collection");
        }
    }
}
