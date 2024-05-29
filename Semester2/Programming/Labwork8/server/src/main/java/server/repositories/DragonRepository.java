package server.repositories;

import common.domain.Coordinates;
import common.domain.Dragon;
import common.domain.DragonHead;
import common.exceptions.BadOwnerException;
import common.user.User;
import org.slf4j.Logger;
import server.App;
import server.managers.PersistenceManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DragonRepository {
    private final Logger logger = App.logger;

    private Set<Dragon> collection = new TreeSet<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final PersistenceManager persistenceManager;

    public DragonRepository(PersistenceManager persistenceManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.persistenceManager = persistenceManager;

        try {
            load();
        } catch (Exception e) {
            logger.error("Error loading data from DB!", e);
            System.exit(3);
        }

        if (!validateAll()) {
            logger.error("Invalid elements!");
            System.exit(2);
        }
    }

    public boolean validateAll() {
        for (var dragon : collection) {
            if (!dragon.validate()) {
                logger.warn("Dragon with id={} is invalid.", dragon.id());
                return false;
            }
            if (dragon.head() != null) {
                if (!dragon.head().validate()) {
                    logger.warn("Head with id={} is invalid.", dragon.id());
                    return false;
                }
            }
        }
        logger.info("All dragons are valid.");
        return true;
    }

    public Set<Dragon> get() {
        return collection;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public String type() {
        return collection.getClass().getName();
    }

    public int size() {
        return collection.size();
    }

    public Dragon first() {
        if (collection.isEmpty()) return null;
        return sorted().get(0);
    }

    public List<Dragon> sorted() {
        return new ArrayList<>(collection)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Dragon getById(int id) {
        return collection
                .stream()
                .takeWhile(d -> d.id() != id)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkExist(int id) {
        return getById(id) != null;
    }

    public synchronized int add(User user, Dragon element) {
        var newId = persistenceManager.add(user, element);
        collection.add(element.copy(newId, user.withoutPassword()));
        lastSaveTime = LocalDateTime.now();
        logger.info("New dragon added successfully.");
        return newId;
    }

    public synchronized void update(User user, Dragon element) throws BadOwnerException {
        var product = getById(element.id());
        if (product == null) {
            add(user, element);
        } else if (product.creator().id() == user.id()) {
            logger.info("Updating product with id={}.", product.id());

            var headId = persistenceManager.update(element);

            if (headId != -1) element.head().setId(headId);
            getById(element.id()).update(element);
            lastSaveTime = LocalDateTime.now();

            logger.info("Product added successfully");
        } else {
            logger.warn("Wrong owner for dragon with id={}.", product.id());
            throw new BadOwnerException();
        }
    }

    public synchronized int remove(User user, int id) throws BadOwnerException {
        if (getById(id).creator().id() != user.id()) {
            logger.warn("Wrong owner for dragon with id={}.", id);
            throw new BadOwnerException();
        }

        var removedCount = persistenceManager.remove(user, id);
        if (removedCount == 0) {
            logger.warn("No elements were removed");
            return 0;
        }

        collection.removeIf(d -> d.id() == id && d.creator().id() == user.id());
        lastSaveTime = LocalDateTime.now();

        return removedCount;
    }

    public synchronized void clear(User user) {
        persistenceManager.clear(user);

        collection.removeIf(d -> d.creator().id() == user.id());
        lastSaveTime = LocalDateTime.now();
    }

    private synchronized void load() {
        logger.info("Loading data");

        collection = new TreeSet<>();
        var daos = persistenceManager.loadDragon();

        var dragons = daos.stream().map((dao) -> {
            DragonHead head = null;
            if (dao.getHead() != null) {
                head = new DragonHead(
                        dao.getHead().getEyeCount()
                );
            }
            return new Dragon(
                    dao.getId(),
                    dao.getName(),
                    new Coordinates(dao.getX(), dao.getY()),
                    dao.getCreationDate(),
                    dao.getAge(),
                    dao.getColor(),
                    dao.getType(),
                    dao.getCharacter(),
                    head,
                    new User(dao.getCreator().getId(), dao.getCreator().getName(), "")
            );
        }).toList();

        collection.addAll(dragons);
        lastInitTime = LocalDateTime.now();

        logger.info("Data loaded");
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Collection is empty!";
        var info = new StringJoiner("\n\n");
        for (var d : collection) {
            info.add(d.toString());
        }
        return info.toString();
    }
}
