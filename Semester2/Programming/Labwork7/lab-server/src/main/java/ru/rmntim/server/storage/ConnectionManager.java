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
import java.time.ZonedDateTime;
import java.util.TreeSet;

public final class ConnectionManager implements AutoCloseable {
    private static ConnectionManager instance;
    private static Connection connection;

    private ConnectionManager() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/lab";
        String username = "postgres";
        String password = "postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    public static synchronized ConnectionManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionManager();
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
    private static Dragon getDragon(ResultSet rs) throws SQLException {
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
    private static DragonHead getDragonHead(int id) throws SQLException {
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
    private static Coordinates getCoordinates(int id) throws SQLException {
        try (var stmt2 = connection.prepareStatement("SELECT * FROM coordinates WHERE dragon_id = ?")) {
            stmt2.setInt(1, id);
            var crs = stmt2.executeQuery();
            if (!crs.next()) {
                throw new SQLException("Can't read coordinates for dragon " + id);
            }
            return new Coordinates(crs.getFloat("x"), crs.getFloat("y"));
        }
    }

}
