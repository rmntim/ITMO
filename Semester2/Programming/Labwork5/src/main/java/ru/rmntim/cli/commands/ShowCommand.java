package ru.rmntim.cli.commands;

import ru.rmntim.cli.logic.CollectionManager;

import java.util.List;

public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(final CollectionManager collectionManager) {
        super("show", "displays all elements of collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new IllegalArgumentException(getName() + " doesn't accept any arguments");
        }
        var collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            System.out.println("Collection is empty");
        } else {
            collection.forEach(System.out::println);
        }
    }
}
