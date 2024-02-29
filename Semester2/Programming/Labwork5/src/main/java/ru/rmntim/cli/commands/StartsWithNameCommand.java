package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.ExecutionContext;

import java.util.List;

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
