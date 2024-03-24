package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;
import ru.rmntim.server.logic.parsers.DragonParser;

import java.util.List;

/**
 * Add an element to the collection if it's less than the minimum
 */
public class AddIfMinCommand extends Command {
    private final CollectionManager collectionManager;

    public AddIfMinCommand(final CollectionManager collectionManager) {
        super("add_if_min", "adds new element to collection if it's less than minimum element");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }

        var comparator = collectionManager.getCollection().comparator();
        if (comparator == null) {
            throw new BadCommandArgumentsException("Collection doesn't have comparator");
        }
        var min = collectionManager.getCollection().stream().min(comparator)
                .orElseThrow(() -> new BadCommandArgumentsException("Collection doesn't have minimum value"));
        var element = new DragonParser(context, collectionManager.getLastSavedId() + 1).parse();

        if (element.compareTo(min) < 0) {
            collectionManager.add(element);
        } else {
            System.out.println("Element is not less than minimum");
        }
    }
}
