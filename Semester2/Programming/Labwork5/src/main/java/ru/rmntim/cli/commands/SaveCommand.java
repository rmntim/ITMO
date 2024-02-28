package ru.rmntim.cli.commands;

import com.google.gson.JsonIOException;
import ru.rmntim.cli.exceptions.BadCommandArgumentsException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.storage.JsonStorageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class SaveCommand extends Command {
    private final CollectionManager collectionManager;
    private final JsonStorageManager storageManager;

    public SaveCommand(final JsonStorageManager storageManager, final CollectionManager collectionManager) {
        super("save", "saves the current collection to the storage");
        this.collectionManager = collectionManager;
        this.storageManager = storageManager;
    }

    @Override
    public void execute(final List<String> arguments, final BufferedReader reader) {
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
