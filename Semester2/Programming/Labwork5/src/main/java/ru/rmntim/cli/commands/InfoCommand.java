package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;

import java.util.List;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(final CollectionManager collectionManager) {
        super("info", "prints information about collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        System.out.println(collectionManager.getCollectionInfo());
    }
}
