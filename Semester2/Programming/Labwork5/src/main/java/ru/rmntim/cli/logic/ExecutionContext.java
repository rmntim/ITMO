package ru.rmntim.cli.logic;

import ru.rmntim.cli.commands.Command;

import java.io.BufferedReader;
import java.util.Map;

public class ExecutionContext {
    private final BufferedReader reader;
    private final Map<String, Command> commands;
    private boolean inFile;

    public ExecutionContext(final BufferedReader reader, final Map<String, Command> commands) {
        this.reader = reader;
        this.commands = commands;
        this.inFile = false;
    }

    public ExecutionContext(final BufferedReader reader, final Map<String, Command> commands, boolean inFile) {
        this.reader = reader;
        this.commands = commands;
        this.inFile = inFile;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public boolean isInFile() {
        return inFile;
    }

    public boolean isInteractive() {
        return !inFile;
    }

    public void setInFile(boolean inFile) {
        this.inFile = inFile;
    }

    public void setInteractive(boolean interactive) {
        this.inFile = !interactive;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
