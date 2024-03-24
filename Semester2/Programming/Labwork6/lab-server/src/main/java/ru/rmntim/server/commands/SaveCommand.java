package ru.rmntim.server.commands;

import com.google.gson.JsonIOException;
import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.ExecutionContext;
import ru.rmntim.server.storage.JsonStorageManager;

import java.io.IOException;
import java.util.List;

/**
 * Saves the collection to file
 */
public class SaveCommand extends Command {
    private final CollectionManager collectionManager;
    private final JsonStorageManager storageManager;

    public SaveCommand(final JsonStorageManager storageManager, final CollectionManager collectionManager) {
        super("save", "saves the current collection to the storage");
        this.collectionManager = collectionManager;
        this.storageManager = storageManager;
    }

    @Override
    public void execute(final List<String> arguments, ExecutionContext context) {
        if (!arguments.isEmpty()) {
            throw new BadCommandArgumentsException(getName() + " requires no arguments");
        }
        try {
            storageManager.writeCollection(collectionManager);
        } catch (IOException | JsonIOException e) {
            System.out.println("Error save collection: " + e.getMessage());
        }
    }
}
