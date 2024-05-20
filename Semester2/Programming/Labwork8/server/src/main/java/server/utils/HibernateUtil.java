package server.utils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import server.App;
import server.dao.OrganizationDAO;
import server.dao.ProductDAO;
import server.dao.UserDAO;

import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = App.logger;

    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory(String url, String user, String password) {
        try {
            var configuration = getConfiguration(url, user, password);

            configuration.addPackage("server.dao");
            configuration.addAnnotatedClass(UserDAO.class);
            configuration.addAnnotatedClass(OrganizationDAO.class);
            configuration.addAnnotatedClass(ProductDAO.class);

            var serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            logger.info("Hibernate Java Config serviceRegistry created");

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException e) {
            logger.error("Initial SessionFactory creation failed", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Configuration getConfiguration(String url, String user, String password) {
        var props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

        props.put("hibernate.connection.username", user);
        props.put("hibernate.connection.password", password);
        props.put("hibernate.connection.url", url);

        props.put("hibernate.connection.pool_size", "100");
        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.connection.autocommit", "true");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
        props.put("hibernate.hbm2ddl.auto", "validate");

        var configuration = new Configuration();
        configuration.setProperties(props);
        return configuration;
    }

    public static SessionFactory getSessionFactory(String url, String user, String password) {
        if (sessionFactory == null) sessionFactory = buildSessionFactory(url, user, password);
        return sessionFactory;
    }
}
