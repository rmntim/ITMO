package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.logic.parsers.DragonParser;

import java.util.List;

/**
 * Add an element to the collection if it's greater than the maximum
 */
public class AddIfMaxCommand extends Command {
    private final CollectionManager collectionManager;

    public AddIfMaxCommand(final CollectionManager collectionManager) {
        super("add_if_max", "adds new element to collection if it's greater than maximum element");
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
        var max = collectionManager.getCollection().stream().max(comparator)
                .orElseThrow(() -> new BadCommandArgumentsException("Collection doesn't have maximum value"));
        var element = new DragonParser(context, collectionManager.getLastSavedId() + 1).parse();

        if (element.compareTo(max) > 0) {
            collectionManager.add(element);
        } else {
            System.out.println("Element is not greater than maximum");
        }
    }
}
