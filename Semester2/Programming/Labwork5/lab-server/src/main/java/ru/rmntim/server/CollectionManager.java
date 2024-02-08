package ru.rmntim.server;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.validation.DragonValidator;
import ru.rmntim.server.storage.StorageManager;

import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollectionManager {
    private static final Logger LOGGER = Logger.getLogger(CollectionManager.class.getName());
    private final StorageManager storageManager;
    private TreeSet<Dragon> collection;

    public CollectionManager(final StorageManager storageManager) {
        this.storageManager = storageManager;
        this.collection = storageManager.readCollection().orElse(new TreeSet<>());
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    public void validateCollection() {
        var validator = new DragonValidator();
        collection = collection.stream().filter(dragon -> {
            if (!validator.validate(dragon)) {
                LOGGER.log(Level.WARNING, "Dragon with id " + dragon.id() + " is not valid");
                return false;
            }
            return true;
        }).collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    }

    public void writeCollection() {
        storageManager.writeCollection(collection);
    }
}
