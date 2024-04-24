package ru.rmntim.server.lib;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.models.DragonType;
import ru.rmntim.common.validators.DragonValidator;
import ru.rmntim.common.validators.ValidationException;
import ru.rmntim.server.storage.DatabaseManager;
import ru.rmntim.server.storage.User;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectionManager {
    private final DatabaseManager databaseManager;
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime lastInitTime = ZonedDateTime.now();

    /**
     * @param databaseManager connection manager with database to read collection from
     * @throws SQLException        if collection can't be read
     * @throws ValidationException if collection is invalid
     */
    public CollectionManager(DatabaseManager databaseManager) throws ValidationException, SQLException {
        this.databaseManager = databaseManager;
        this.collection = databaseManager.readCollection();
    }

    /**
     * Returns info about collection.
     */
    public synchronized String getCollectionInfo() {
        return "Collection size: " + collection.size() + "\n"
                + "Initialized at: " + lastInitTime + "\n"
                + "Collection type: " + collection.getClass().getSimpleName() + "\n";
    }

    /**
     * @return string representation of the collection
     */
    public synchronized String collectionString() {
        if (collection.isEmpty()) {
            return "Collection is empty";
        } else {
            var sj = new StringJoiner("\n");
            collection.forEach(dragon -> sj.add(dragon.toString()));
            return sj.toString();
        }
    }

    /**
     * Validates and adds dragon to the collection.
     *
     * @param dragon   dragon to add
     * @param username username
     * @throws ValidationException if dragon is invalid
     * @throws SQLException        if dragon can't be added
     */
    public synchronized int add(Dragon dragon, String username) throws ValidationException, SQLException {
        new DragonValidator().validate(dragon);
        var id = databaseManager.addDragon(dragon, username);
        collection.add(dragon);
        return id;
    }

    /**
     * Updates element with given id.
     *
     * @param id       id of the element to update
     * @param dragon   dragon to update
     * @param username username
     * @throws ValidationException if element is invalid
     * @throws SQLException        if dragon can't be updated
     */
    public synchronized void update(int id, Dragon dragon, String username) throws ValidationException, SQLException {
        new DragonValidator().validate(dragon);
        databaseManager.updateDragon(id, dragon, username);
        collection.removeIf(e -> e.id() == id);
        dragon.setId(id);
        collection.add(dragon);
    }

    /**
     * Remove element with given id.
     *
     * @param id       id of the element to remove
     * @param username username
     * @throws SQLException if dragon can't be removed
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public synchronized void remove(int id, String username) throws SQLException {
        databaseManager.removeDragon(id, username);
        collection.removeIf(e -> e.id() == id);
    }

    /**
     * Clear the collection.
     *
     * @param username username
     * @throws SQLException if coordinates can't be read
     */
    public synchronized void clear(String username) throws SQLException {
        databaseManager.clear(username);
        collection.clear();
    }

    /**
     * Adds new element to the collection if it's greater than current maximum.
     * If collection is empty, adds element unconditionally.
     *
     * @param dragon   dragon to add
     * @param username username
     * @throws ValidationException if element is invalid
     * @throws SQLException        if dragon can't be added
     */
    public synchronized void addIfMax(Dragon dragon, String username) throws ValidationException, SQLException {
        Dragon max;
        try {
            max = collection.stream().max(Dragon::compareTo).orElseThrow();
            if (dragon.compareTo(max) <= 0) {
                return;
            }
        } catch (NoSuchElementException ignored) {
            // ignore
        }
        add(dragon, username);
    }

    /**
     * Adds new element to the collection if it's lower than current minimum.
     * If collection is empty, adds element unconditionally.
     *
     * @param dragon   dragon to add
     * @param username username
     * @throws ValidationException if element is invalid
     * @throws SQLException        if dragon can't be added
     */
    public synchronized void addIfMin(Dragon dragon, String username) throws ValidationException, SQLException {
        Dragon min;
        try {
            min = collection.stream().min(Dragon::compareTo).orElseThrow();
            if (dragon.compareTo(min) >= 0) {
                return;
            }
        } catch (NoSuchElementException ignored) {
            // ignore
        }
        add(dragon, username);
    }

    /**
     * Removes all elements from the collection lower that given.
     *
     * @param dragon   dragon to compare to
     * @param username username
     * @throws ValidationException if element is invalid
     * @throws SQLException        if dragons can't be removed
     */
    public synchronized void removeIfLower(Dragon dragon, String username) throws ValidationException, SQLException {
        new DragonValidator().validate(dragon);
        var ids = collection.stream()
                .filter(e -> e.compareTo(dragon) < 0)
                .map(Dragon::id)
                .collect(Collectors.toSet());
        databaseManager.removeDragons(ids, username);
        collection.removeIf(e -> ids.contains(e.id()));
    }

    /**
     * Groups elements by type and displays number of elements in each group.
     *
     * @return grouped elements
     */
    public synchronized Map<DragonType, Integer> groupByType() {
        return collection.stream()
                .collect(Collectors.groupingBy(Dragon::type))
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().size())
                );
    }

    /**
     * Displays number of elements with character greater that given.
     *
     * @param dragon element to compare to
     * @return number of elements
     */
    public synchronized long greaterThanCharacter(Dragon dragon) throws ValidationException {
        new DragonValidator().validate(dragon);
        return collection.stream().filter(e -> e.character().compareTo(dragon.character()) > 0).count();
    }

    /**
     * Displays all elements with name starting with given string.
     *
     * @param prefix prefix to search for
     * @return elements with given prefix
     */
    public synchronized TreeSet<Dragon> startsWith(String prefix) {
        return collection.stream()
                .filter(e -> e.name().startsWith(prefix))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Gets user from database.
     *
     * @param username username
     * @return user
     */
    public Optional<User> getUser(String username) {
        try {
            return databaseManager.getUser(username);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
