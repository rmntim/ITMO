package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.DragonBuilder;

import java.io.BufferedReader;
import java.util.List;

public class AddIfMinCommand extends Command {
    private final CollectionManager collectionManager;

    public AddIfMinCommand(final CollectionManager collectionManager) {
        super("add_if_min", "adds new element to collection if it's less than minimum");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, final BufferedReader reader) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }

        var comparator = collectionManager.getCollection().comparator();
        if (comparator == null) {
            throw new BadCommandArgumentsException("Collection doesn't have comparator");
        }
        var min = collectionManager.getCollection().stream().min(comparator)
                .orElseThrow(() -> new BadCommandArgumentsException("Collection doesn't have minimum value"));
        var element = DragonBuilder.build(collectionManager.getLastSavedId() + 1, reader);

        if (element.compareTo(min) < 0) {
            collectionManager.add(element);
        } else {
            System.out.println("Element is not less than minimum");
        }
    }
}
