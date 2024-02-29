package ru.rmntim.cli.logic;

import ru.rmntim.cli.commands.Command;

import java.io.BufferedReader;
import java.util.Map;

public class ExecutionContext {
    private final BufferedReader reader;
    private final Map<String, Command> commands;
    private final boolean inFile;
    private String cwd = System.getProperty("user.dir");

    public ExecutionContext(final BufferedReader reader, final Map<String, Command> commands) {
        this.reader = reader;
        this.commands = commands;
        this.inFile = false;
    }

    public ExecutionContext(final BufferedReader reader, final Map<String, Command> commands, String cwd, boolean inFile) {
        this.reader = reader;
        this.commands = commands;
        this.cwd = cwd;
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

    public Map<String, Command> getCommands() {
        return commands;
    }

    public String getCwd() {
        return cwd;
    }
}
