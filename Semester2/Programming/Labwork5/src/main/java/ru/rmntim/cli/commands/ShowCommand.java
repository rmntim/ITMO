package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;

import java.io.InputStream;
import java.util.List;

public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(final CollectionManager collectionManager) {
        super("show", "displays all elements of collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, final InputStream inputStream) {
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
