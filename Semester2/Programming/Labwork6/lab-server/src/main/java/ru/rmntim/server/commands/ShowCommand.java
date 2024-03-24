package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;

import java.util.List;

/**
 * Displays all elements in collection
 */
public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(final CollectionManager collectionManager) {
        super("show", "displays all elements of collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        var collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            System.out.println("Collection is empty");
        } else {
            collection.forEach(System.out::println);
        }
    }
}
