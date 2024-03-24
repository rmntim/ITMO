package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;

import java.util.List;

/**
 * Displays all elements which names start with the specified string
 */
public class StartsWithNameCommand extends Command {
    private final CollectionManager collectionManager;

    public StartsWithNameCommand(final CollectionManager collectionManager) {
        super("filter_starts_with_name", "show all elements where name starts with given string", List.of("name"));
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(List<String> arguments, ExecutionContext context) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " requires one argument");
        }

        try {
            var name = arguments.get(0);
            collectionManager.getCollection().stream()
                    .filter(e -> e.name().startsWith(name))
                    .forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            throw new BadCommandArgumentsException("Invalid character");
        }
    }
}
