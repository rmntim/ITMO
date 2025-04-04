package server;

import common.utility.CommandName;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.*;
import server.handlers.CommandHandler;
import server.managers.AuthManager;
import server.managers.CommandManagerBuilder;
import server.managers.PersistenceManager;
import server.network.UDPDatagramServer;
import server.repositories.DragonRepository;
import server.utils.HibernateUtil;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

public class App {
    public static final int PORT = 8080;
    private static final String ENV_FILE = ".env";

    public static final Logger logger = LoggerFactory.getLogger("ServerLogger");
    public static Dotenv dotenv;

    public static void main(String[] args) {
        try {
            var sessionFactory = (SessionFactoryImpl) getHibernateSessionFactory();
            Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));

            var persistenceManager = new PersistenceManager(sessionFactory);
            var authManager = new AuthManager(sessionFactory, dotenv.get("PEPPER"));

            var repository = new DragonRepository(persistenceManager);
            var commands = initializeCommands(repository, authManager);

            var server = new UDPDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commands, authManager));
            server.run();
        } catch (SocketException e) {
            logger.error("Socket error", e);
        } catch (UnknownHostException e) {
            logger.error("Unknown host", e);
        } catch (ExceptionInInitializerError e) {
            logger.error("Error initializing Hibernate", e);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted", e);
        }
    }

    private static SessionFactory getHibernateSessionFactory() {
        loadEnv();
        var url = dotenv.get("DB_URL");
        var user = dotenv.get("DB_USER");
        var password = dotenv.get("DB_PASSWORD");

        if (url == null || url.isEmpty() || user == null || user.isEmpty() || password == null || password.isEmpty()) {
            System.out.println(".env file must contain DB_URL, DB_USER and DB_PASSWORD");
            System.exit(1);
        }

        return HibernateUtil.getSessionFactory(url, user, password);
    }

    private static Map<String, Command> initializeCommands(DragonRepository repository, AuthManager authManager) {
        return new CommandManagerBuilder()
                .register(CommandName.REGISTER, new Register(authManager))
                .register(CommandName.AUTHENTICATE, new Authenticate(authManager))
                .register(CommandName.INFO, new Info(repository))
                .register(CommandName.SHOW, new Show(repository))
                .register(CommandName.ADD, new Add(repository))
                .register(CommandName.UPDATE, new Update(repository))
                .register(CommandName.REMOVE_BY_ID, new RemoveById(repository))
                .register(CommandName.CLEAR, new Clear(repository))
                .register(CommandName.ADD_IF_MAX, new AddIfMax(repository))
                .register(CommandName.ADD_IF_MIN, new AddIfMin(repository))
                .build();
    }

    private static void loadEnv() {
        dotenv = Dotenv.configure().filename(ENV_FILE).ignoreIfMalformed().ignoreIfMissing().load();
    }
}
