package ru.rmntim.server.commands;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.exceptions.BuildCancelledException;
import ru.rmntim.server.exceptions.InvalidScriptException;
import ru.rmntim.server.exceptions.RecursionException;
import ru.rmntim.server.logic.ExecutionContext;
import ru.rmntim.server.logic.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Executes provided script
 */
public class ExecuteCommand extends Command {
    private Map<String, Command> commands;
    private final Set<String> visitedFiles = new HashSet<>();

    public ExecuteCommand() {
        super("execute_script", "executes given script", List.of("file_name"));
    }

    public void setCommands(final Map<String, Command> commands) {
        this.commands = commands;
    }

    /**
     * @param arguments list of arguments to the command, not including the command name
     * @param context   execution context
     * @throws RecursionException     if any of the scripts contains recursion
     * @throws InvalidScriptException if any of the scripts contains invalid syntax
     */
    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " accepts only one argument");
        }

        var fileName = arguments.get(0);
        var file = new File(context.getCwd() + File.separator + fileName);
        var parent = file.getAbsoluteFile().getParent();

        if (!file.isFile()) {
            throw new BadCommandArgumentsException(fileName + " is not a valid file");
        }
        if (visitedFiles.contains(fileName)) {
            throw new RecursionException(fileName);
        }

        try {
            var fileReader = new BufferedReader(new FileReader(file));
            var ctx = new ExecutionContext(fileReader, commands, parent, true);
            visitedFiles.add(fileName);
            new Interpreter(ctx).run();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        } catch (BuildCancelledException bce) {
            throw new InvalidScriptException();
        } finally {
            visitedFiles.remove(fileName);
        }
    }
}
