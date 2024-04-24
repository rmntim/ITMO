package ru.rmntim.server.storage;

import ru.rmntim.common.models.Color;
import ru.rmntim.common.models.Coordinates;
import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.models.DragonCharacter;
import ru.rmntim.common.models.DragonHead;
import ru.rmntim.common.models.DragonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.TreeSet;

public final class DatabaseManager implements AutoCloseable {
    private static DatabaseManager instance;
    private static Connection connection;

    private DatabaseManager() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/lab";
        String username = "postgres";
        String password = "postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    public static synchronized DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * Reads collection from database.
     *
     * @return collection
     */
    public TreeSet<Dragon> readCollection() throws SQLException {
        var collection = new TreeSet<Dragon>();
        var conn = getConnection();
        try (var stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT * FROM dragons");
            while (rs.next()) {
                var dragon = getDragon(rs);
                collection.add(dragon);
            }
        }
        return collection;
    }

    /**
     * Reads dragon from result set.
     *
     * @param rs result set
     * @return dragon
     * @throws SQLException if dragon can't be read
     */
    private Dragon getDragon(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var name = rs.getString("name");
        var coordinates = getCoordinates(id);
        var creationDate = rs.getTimestamp("creation_date").toInstant().atZone(ZonedDateTime.now().getZone());
        var age = rs.getLong("age");
        var color = Color.valueOf(rs.getString("color"));
        var type = DragonType.valueOf(rs.getString("dragon_type"));
        var character = DragonCharacter.valueOf(rs.getString("dragon_character"));
        var head = getDragonHead(id);
        return new Dragon(id, name, coordinates, creationDate, age, color, type, character, head);
    }

    /**
     * Gets dragon head for given id.
     *
     * @param id dragon id
     * @return dragon head for given id
     * @throws SQLException if dragon head can't be read
     */
    private DragonHead getDragonHead(int id) throws SQLException {
        try (var stmt3 = connection.prepareStatement("SELECT * FROM dragon_heads WHERE dragon_id = ?")) {
            stmt3.setInt(1, id);
            var hrs = stmt3.executeQuery();
            if (hrs.next()) {
                return new DragonHead(hrs.getDouble("eyes_count"));
            }
            return null;
        }
    }

    /**
     * Gets coordinates for given id.
     *
     * @param id id of the dragon
     * @return coordinates of the dragon
     * @throws SQLException if coordinates can't be read
     */
    private Coordinates getCoordinates(int id) throws SQLException {
        try (var stmt2 = connection.prepareStatement("SELECT * FROM coordinates WHERE dragon_id = ?")) {
            stmt2.setInt(1, id);
            var crs = stmt2.executeQuery();
            if (!crs.next()) {
                throw new SQLException("Can't read coordinates for dragon " + id);
            }
            return new Coordinates(crs.getFloat("x"), crs.getFloat("y"));
        }
    }

    /**
     * Adds dragon to database.
     *
     * @param dragon dragon to add
     * @return id of the dragon
     * @throws SQLException if dragon can't be added
     */
    public int addDragon(Dragon dragon) throws SQLException {
        try (var stmt = connection.prepareStatement(
                "INSERT INTO dragons (name, creation_date, age, color, dragon_type, dragon_character) VALUES (?, ?, ?, ?, ?, ?) RETURNING id"
        )
        ) {
            stmt.setString(1, dragon.name());
            stmt.setTimestamp(2, Timestamp.from(dragon.creationDate().toInstant()));
            stmt.setLong(3, dragon.age());
            stmt.setString(4, dragon.color().name());
            stmt.setString(5, dragon.type().name());
            stmt.setString(6, dragon.character().name());
            var rs = stmt.executeQuery();
            if (rs.next()) {
                var id = rs.getInt(1);
                dragon.setId(id);
            }
        }
        addCoordinates(dragon);
        if (dragon.head() != null) {
            addDragonHead(dragon);
        }
        return dragon.id();
    }

    /**
     * Adds dragon head to database.
     *
     * @param dragon dragon to add
     * @throws SQLException if dragon head can't be added
     */
    private void addDragonHead(Dragon dragon) throws SQLException {
        try (var stmt3 = connection.prepareStatement("INSERT INTO dragon_heads (dragon_id, eyes_count) VALUES (?, ?)")) {
            stmt3.setInt(1, dragon.id());
            stmt3.setDouble(2, dragon.head().eyesCount());
            stmt3.executeUpdate();
        }
    }

    /**
     * Adds coordinates to database.
     *
     * @param dragon dragon to add
     * @throws SQLException if coordinates can't be added
     */
    private void addCoordinates(Dragon dragon) throws SQLException {
        try (var stmt2 = connection.prepareStatement("INSERT INTO coordinates (dragon_id, x, y) VALUES (?, ?, ?)")) {
            stmt2.setInt(1, dragon.id());
            stmt2.setFloat(2, dragon.coordinates().x());
            stmt2.setFloat(3, dragon.coordinates().y());
            stmt2.executeUpdate();
        }
    }

    /**
     * Updates dragon in database.
     *
     * @param id     id of the dragon
     * @param dragon dragon to update
     * @throws SQLException if dragon can't be updated
     */
    public void updateDragon(int id, Dragon dragon) throws SQLException {
        try (var stmt = connection.prepareStatement(
                "UPDATE dragons SET name = ?, creation_date = ?, age = ?, color = ?, dragon_type = ?, dragon_character = ? WHERE id = ?"
        )
        ) {
            stmt.setString(1, dragon.name());
            stmt.setTimestamp(2, Timestamp.from(dragon.creationDate().toInstant()));
            stmt.setLong(3, dragon.age());
            stmt.setString(4, dragon.color().name());
            stmt.setString(5, dragon.type().name());
            stmt.setString(6, dragon.character().name());
            stmt.setInt(7, id);
            stmt.executeUpdate();
        }
        updateCoordinates(id, dragon.coordinates());
        if (dragon.head() != null) {
            updateDragonHead(id, dragon.head());
        }
    }

    /**
     * Updates dragon head in database.
     *
     * @param id         id of the dragon
     * @param dragonHead dragon head to update
     * @throws SQLException if dragon head can't be updated
     */
    private void updateDragonHead(int id, DragonHead dragonHead) throws SQLException {
        try (var stmt3 = connection.prepareStatement("UPDATE dragon_heads SET eyes_count = ? WHERE dragon_id = ?")) {
            stmt3.setDouble(1, dragonHead.eyesCount());
            stmt3.setInt(2, id);
            stmt3.executeUpdate();
        }
    }

    /**
     * Updates coordinates in database.
     *
     * @param id          id of the dragon
     * @param coordinates coordinates to update
     * @throws SQLException if coordinates can't be updated
     */
    private void updateCoordinates(int id, Coordinates coordinates) throws SQLException {
        try (var stmt2 = connection.prepareStatement("UPDATE coordinates SET x = ?, y = ? WHERE dragon_id = ?")) {
            stmt2.setFloat(1, coordinates.x());
            stmt2.setFloat(2, coordinates.y());
            stmt2.setInt(3, id);
            stmt2.executeUpdate();
        }
    }

    /**
     * Removes dragon from database.
     *
     * @param id id of the dragon
     * @throws SQLException if dragon head can't be read
     */
    public void removeDragon(int id) throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM dragons WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        try (var stmt2 = connection.prepareStatement("DELETE FROM coordinates WHERE dragon_id = ?")) {
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
        }
        if (getDragonHead(id) != null) {
            try (var stmt3 = connection.prepareStatement("DELETE FROM dragon_heads WHERE dragon_id = ?")) {
                stmt3.setInt(1, id);
                stmt3.executeUpdate();
            }
        }
    }

    /**
     * Clears database.
     *
     * @throws SQLException if coordinates can't be read
     */
    public void clear() throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM dragons")) {
            stmt.executeUpdate();
        }
        try (var stmt2 = connection.prepareStatement("DELETE FROM coordinates")) {
            stmt2.executeUpdate();
        }
        try (var stmt3 = connection.prepareStatement("DELETE FROM dragon_heads")) {
            stmt3.executeUpdate();
        }
    }

    /**
     * Removes dragons from database.
     *
     * @param ids ids of dragons to remove
     * @throws SQLException if dragons can't be removed
     */
    public void removeDragons(Set<Integer> ids) throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM dragons WHERE id = ANY(?)")) {
            stmt.setArray(1, connection.createArrayOf("integer", ids.toArray()));
            stmt.executeUpdate();
        }
    }
}
