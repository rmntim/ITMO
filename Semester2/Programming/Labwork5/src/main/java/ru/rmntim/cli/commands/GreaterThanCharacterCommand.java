package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.models.DragonCharacter;

import java.util.List;

/**
 * Displays amount of elements in collection with character greater than specified
 */
public class GreaterThanCharacterCommand extends Command {
    private final CollectionManager collectionManager;

    public GreaterThanCharacterCommand(final CollectionManager collectionManager) {
        super("count_greater_than_character", "counts all elements greater than given character", List.of("character"));
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " requires 1 argument");
        }
        try {
            var character = DragonCharacter.valueOf(arguments.get(0).toUpperCase());
            System.out.println(collectionManager.getCollection().stream()
                    .filter(d -> d.character().compareTo(character) > 0)
                    .count());
        } catch (IllegalArgumentException e) {
            throw new BadCommandArgumentsException("Invalid character");
        }
    }
}
