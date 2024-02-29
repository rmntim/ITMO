package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.logic.parsers.DragonParser;
import ru.rmntim.cli.models.Dragon;

import java.util.List;

/**
 * Removes all elements less than specified
 */
public class RemoveLowerCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(final CollectionManager collectionManager) {
        super("remove_lower", "removes all elements less than given");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }

        var element = new DragonParser(context, collectionManager.getLastSavedId() + 1).parse();
        var toRemove = collectionManager.getCollection().stream()
                .filter(dragon -> dragon.compareTo(element) < 0)
                .map(Dragon::id)
                .toList();

        for (var id : toRemove) {
            collectionManager.remove(id);
        }
    }
}
