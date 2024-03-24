package ru.rmntim.client.logic;

import ru.rmntim.client.commands.Command;
import ru.rmntim.client.network.UDPClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Manages the execution context of the interpreter.
 */
public class ExecutionContext {
    private final UDPClient client;
    private final BufferedReader reader;
    private final Map<String, Command> commands;
    private boolean inFile = false;

    /**
     * Creates a new execution context.
     */
    public ExecutionContext(final UDPClient client, final InputStream inputStream, final Map<String, Command> commands) {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.commands = commands;
    }

    /**
     * @return true if the interpreter is in the file mode
     */
    public boolean isInFile() {
        return inFile;
    }

    public void setInFile(boolean inFile) {
        this.inFile = inFile;
    }

    public UDPClient getClient() {
        return client;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
