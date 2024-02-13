package ru.rmntim.server;

import ru.rmntim.common.commands.Command;
import ru.rmntim.common.commands.CommandVisitor;
import ru.rmntim.common.commands.ExitCommand;
import ru.rmntim.common.commands.HelpCommand;
import ru.rmntim.common.commands.StatusCode;

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
}
