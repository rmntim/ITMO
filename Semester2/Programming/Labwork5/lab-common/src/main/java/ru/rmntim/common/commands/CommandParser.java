package ru.rmntim.common.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CommandParser {
    private final HashMap<String, Command> commands;

    public CommandParser(final HashMap<String, Command> commands) {
        this.commands = commands;
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
            case "exit" -> {
                if (arguments.isPresent()) {
                    yield Optional.empty();
                }
                yield Optional.ofNullable(commands.get("exit"));
            }
            case "help" -> {
                if (arguments.isPresent()) {
                    yield Optional.empty();
                }
                yield Optional.ofNullable(commands.get("help"));
            }
            case "info" -> {
                if (arguments.isPresent()) {
                    yield Optional.empty();
                }
                yield Optional.ofNullable(commands.get("info"));
            }
            case "show" -> {
                if (arguments.isPresent()) {
                    yield Optional.empty();
                }
                yield Optional.ofNullable(commands.get("show"));
            }
            case "add" -> Optional.ofNullable(commands.get("add"));
            default -> Optional.empty();
        };
    }
}
