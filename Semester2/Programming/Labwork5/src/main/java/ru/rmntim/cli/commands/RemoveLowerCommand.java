package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.DragonBuilder;
import ru.rmntim.cli.models.Dragon;

import java.io.BufferedReader;
import java.util.List;

public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(final CollectionManager collectionManager) {
        super("remove_lower", "removes all elements less than given");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, final BufferedReader reader) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }

        var element = DragonBuilder.build(collectionManager.getLastSavedId() + 1, reader);
        var toRemove = collectionManager.getCollection().stream()
                .filter(dragon -> dragon.compareTo(element) < 0)
                .map(Dragon::id)
                .toList();

        for (var id : toRemove) {
            collectionManager.remove(id);
        }
    }
}
