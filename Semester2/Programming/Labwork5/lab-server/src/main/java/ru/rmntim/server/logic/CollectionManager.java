package ru.rmntim.server.logic;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.validation.DragonValidator;
import ru.rmntim.server.storage.StorageManager;

import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollectionManager {
    private static final Logger LOGGER = Logger.getLogger(CollectionManager.class.getName());
    private final LocalDateTime initializationDate;
    private TreeSet<Dragon> collection;

    public CollectionManager(final StorageManager storageManager) {
        this.collection = storageManager.readCollection().orElse(new TreeSet<>());
        this.initializationDate = LocalDateTime.now();
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public void validateCollection() {
        collection = collection.stream().filter(dragon -> {
            if (!DragonValidator.validate(dragon)) {
                LOGGER.log(Level.WARNING, "Dragon with id " + dragon.id() + " is not valid");
                return false;
            }
            return true;
        }).collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    }
}
