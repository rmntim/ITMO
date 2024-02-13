package ru.rmntim.server.logic;

import ru.rmntim.common.commands.AddCommand;
import ru.rmntim.common.commands.Command;
import ru.rmntim.common.commands.ExitCommand;
import ru.rmntim.common.commands.HelpCommand;
import ru.rmntim.common.commands.InfoCommand;
import ru.rmntim.common.commands.ShowCommand;
import ru.rmntim.common.commands.UpdateCommand;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Interpreter implements Command.Visitor<StatusCode> {
    private final CollectionManager collectionManager;
    private final Map<String, String> availableCommands;

    public Interpreter(final CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.availableCommands = Map.of(
                "exit", "exits the program",
                "help", "shows help",
                "info", "shows information about collection",
                "show", "shows collection",
                "add {element}", "adds an element to the collection",
                "update [ID] {element}", "updates an element in the collection by ID"
        );
    }

    public StatusCode execute(Command command) {
        return command.accept(this);
    }

    @Override
    public StatusCode visit(ExitCommand command) {
        return StatusCode.EXIT;
    }

    @Override
    public StatusCode visit(HelpCommand command) {
        availableCommands.forEach((key, value) -> System.out.println(key + " - " + value));
        return StatusCode.OK;
    }

    @Override
    public StatusCode visit(InfoCommand command) {
        System.out.println("Collection type: " + collectionManager.getCollection().getClass().getSimpleName());
        System.out.println("Initialization date: " + collectionManager.getInitializationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Number of elements: " + collectionManager.getCollection().size());
        return StatusCode.OK;
    }

    @Override
    public StatusCode visit(ShowCommand command) {
        collectionManager.getCollection().forEach(System.out::println);
        return StatusCode.OK;
    }

    @Override
    public StatusCode visit(AddCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public StatusCode visit(UpdateCommand command) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
