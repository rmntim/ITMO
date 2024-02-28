package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.models.Dragon;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;

public class GroupByTypeCommand extends Command {
    private final CollectionManager collectionManager;

    public GroupByTypeCommand(final CollectionManager collectionManager) {
        super("group_counting_by_type", "group elements by type, output each group size");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(List<String> arguments, BufferedReader reader) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }

        var groups = collectionManager.getCollection().stream()
                .collect(Collectors.groupingBy(Dragon::type));

        for (var group : groups.entrySet()) {
            System.out.println(group.getKey() + ": " + group.getValue().size());
        }
    }
}
