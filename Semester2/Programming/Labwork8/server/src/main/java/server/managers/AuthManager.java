package server.managers;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import server.App;
import server.dao.UserDAO;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AuthManager {
    private static final int SALT_LENGTH = 10;
    private final Logger logger = App.logger;
    private final SessionFactory sessionFactory;
    private final String pepper;

    public AuthManager(SessionFactory sessionFactory, String pepper) {
        this.sessionFactory = sessionFactory;
        this.pepper = pepper;
    }

    public int registerUser(String login, String password) {
        logger.info("Creating new user with username {}", login);

        var salt = generateSalt();
        var passwordHash = generatePasswordHash(password, salt);

        var dao = new UserDAO();
        dao.setName(login);
        dao.setPasswordDigest(passwordHash);
        dao.setSalt(salt);

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(dao);
            session.getTransaction().commit();
        }

        var newId = dao.getId();
        logger.info("User created successfully, id={}", newId);
        return newId;
    }

    public int authenticateUser(String login, String password) throws SQLException {
        logger.info("Authenticating user {}", login);
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name", UserDAO.class);
            query.setParameter("name", login);

            var result = query.list();
            if (result.isEmpty()) {
                logger.warn("Wrong password for {}", login);
                return 0;
            }

            var user = result.get(0);
            session.getTransaction().commit();

            var id = user.getId();
            var salt = user.getSalt();
            var expectedHashedPassword = user.getPasswordDigest();

            var actualHashedPassword = generatePasswordHash(password, salt);
            if (expectedHashedPassword.equals(actualHashedPassword)) {
                logger.info("User {} authenticated successfully", login);
                return id;
            }

            return 0;
        }
    }

    private String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
    }

    private String generatePasswordHash(String password, String salt) {
        //noinspection deprecation
        return Hashing.md5()
                .hashString(pepper + password + salt, StandardCharsets.UTF_8)
                .toString();
    }
}
