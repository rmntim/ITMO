package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.logic.parsers.DragonParser;

import java.util.List;

public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateCommand(final CollectionManager collectionManager) {
        super("update", "updates element with specified id", List.of("id"));
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " requires one argument");
        }

        try {
            var id = Integer.parseInt(arguments.get(0));
            collectionManager.update(id, () -> new DragonParser(context, id).parse());
        } catch (NumberFormatException e) {
            throw new BadCommandArgumentsException(arguments.get(0) + " is not a number");
        } catch (IllegalArgumentException e) {
            throw new BadCommandArgumentsException(e.getMessage());
        }
    }
}
