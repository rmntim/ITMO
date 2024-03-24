package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;
import ru.rmntim.server.logic.parsers.DragonParser;

import java.util.List;

/**
 * Updates an element with specified id
 */
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
