package ru.rmntim.cli.commands;

import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.exceptions.RecursionException;
import ru.rmntim.cli.logic.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExecuteCommand extends Command {
    private Map<String, Command> commands;
    private final Set<String> visitedFiles = new HashSet<>();

    public ExecuteCommand() {
        super("execute_script", "executes given script", List.of("file_name"));
    }

    public void setCommands(final Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(List<String> arguments, BufferedReader reader) {
        if (arguments.size() != 1) {
            throw new BadCommandArgumentsException(getName() + " accepts only one argument");
        }
        var interpreter = new Interpreter(commands);
        var oldIn = System.in;
        var fileName = arguments.get(0);
        if (visitedFiles.contains(fileName)) {
            throw new RecursionException(fileName);
        }
        try {
            var file = new File(fileName);
            if (!file.isFile()) {
                throw new BadCommandArgumentsException(fileName + " is not a valid file");
            }

            var inputStream = new FileInputStream(file);
            System.setIn(inputStream);
            visitedFiles.add(fileName);
            interpreter.run();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        } finally {
            System.setIn(oldIn);
        }
    }
}
