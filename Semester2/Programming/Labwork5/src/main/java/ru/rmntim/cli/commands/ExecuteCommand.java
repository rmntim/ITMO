package ru.rmntim.cli.commands;

import ru.rmntim.cli.Interpreter;
import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.exceptions.InvalidScriptException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class ExecuteCommand extends Command {
    private Map<String, Command> commands;

    public ExecuteCommand() {
        super("execute_script", "executes given script");
    }

    public void setCommands(final Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " accepts only one argument");
        }
        var interpreter = new Interpreter(commands);
        var oldIn = System.in;
        try {
            var inputStream = new FileInputStream(arguments.get(0));
            System.setIn(inputStream);
            interpreter.run();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found" + System.getProperty("user.dir"));
        } catch (InvalidScriptException ise) {
            System.out.println("Script format error: " + ise.getMessage());
        } finally {
            System.setIn(oldIn);
        }
    }
}
