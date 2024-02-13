package ru.rmntim.common.commands;

import java.util.List;
import java.util.Optional;

public class CommandParser {
    public CommandParser() {
    }

    public Optional<Command> parse(final List<String> tokens) {
        var commandName = tokens.get(0);
        Optional<String> arguments = Optional.empty();
        try {
            arguments = Optional.of(tokens.get(1));
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }

        return switch (commandName) {
            case "exit" -> arguments.isEmpty() ? Optional.of(new ExitCommand()) : Optional.empty();
            case "help" -> arguments.isEmpty() ? Optional.of(new HelpCommand()) : Optional.empty();
            case "info" -> arguments.isEmpty() ? Optional.of(new InfoCommand()) : Optional.empty();
            case "show" -> arguments.isEmpty() ? Optional.of(new ShowCommand()) : Optional.empty();
            case "add" -> Optional.of(new AddCommand());
            case "update" -> Optional.of(new UpdateCommand(0));
            default -> Optional.empty();
        };
    }
}
