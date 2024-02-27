package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.exceptions.InvalidBuildException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.models.builders.DragonBuilder;

import java.io.InputStream;
import java.util.List;

public class AddCommand extends Command {
    private final CollectionManager collectionManager;

    public AddCommand(final CollectionManager collectionManager) {
        super("add", "adds new element to collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, final InputStream inputStream) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " doesn't accept any arguments");
        }
        try {
            var element = new DragonBuilder(collectionManager.getLastSavedId(), inputStream).build();
            collectionManager.getCollection().add(element);
        } catch (InvalidBuildException bce) {
            System.out.println("Unable to create element");
        } catch (IllegalArgumentException ve) {
            // TODO
        }
    }
}
