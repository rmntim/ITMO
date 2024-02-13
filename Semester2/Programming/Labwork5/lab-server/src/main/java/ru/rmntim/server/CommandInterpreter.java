package ru.rmntim.server;

import ru.rmntim.common.commands.Command;
import ru.rmntim.common.commands.CommandVisitor;
import ru.rmntim.common.commands.ExitCommand;
import ru.rmntim.common.commands.HelpCommand;
import ru.rmntim.common.commands.InfoCommand;
import ru.rmntim.common.commands.StatusCode;

import java.time.format.DateTimeFormatter;

public class CommandInterpreter implements CommandVisitor<StatusCode> {
    public CommandInterpreter() {
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
        command.getCommands().forEach(System.out::println);
        return StatusCode.OK;
    }

    @Override
    public StatusCode visit(InfoCommand command) {
        System.out.println("Collection type: " + command.getCollection().getClass().getSimpleName());
        System.out.println("Initialization date: " + command.getInitializationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Number of elements: " + command.getCollection().size());
        return StatusCode.OK;
    }
}
