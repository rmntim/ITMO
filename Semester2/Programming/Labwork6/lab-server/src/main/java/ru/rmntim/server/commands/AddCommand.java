package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;
import ru.rmntim.server.logic.parsers.DragonParser;

import java.util.List;

/**
 * Adds new element to collection
 */
public class AddCommand extends Command {
    private final CollectionManager collectionManager;

    public AddCommand(final CollectionManager collectionManager) {
        super("add", "adds new element to collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }

        var dragon = new DragonParser(context, collectionManager.getLastSavedId() + 1).parse();
        collectionManager.add(dragon);
    }
}
