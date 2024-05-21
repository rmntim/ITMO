package server.managers;

import common.user.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import server.App;
import server.dao.OrganizationDAO;
import server.dao.ProductDAO;
import server.dao.UserDAO;

import java.util.List;

public class PersistenceManager {
    private final SessionFactory sessionFactory;
    private final Logger logger = App.logger;

    public PersistenceManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int add(User user, Product product) {
        logger.info("Adding new product {}", product.getName());
        OrganizationDAO newOrg = null;
        if (product.getManufacturer() != null) {
            newOrg = addOrganization(user, product.getManufacturer());
        }

        var dao = new ProductDAO(product);
        dao.setManufacturer(newOrg);
        dao.setCreator(new UserDAO(user));

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(dao);
            session.getTransaction().commit();
            logger.info("Product with id {} added successfully.", dao.getId());
            return dao.getId();
        }
    }

    public OrganizationDAO addOrganization(User user, Organization organization) {
        logger.info("Adding new organization {}", organization.getName());

        var dao = new OrganizationDAO(organization);
        dao.setCreator(new UserDAO(user));

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(dao);
            session.getTransaction().commit();
            logger.info("Organization with id {} added successfully.", dao.getId());
            return dao;
        }
    }

    public int update(Product product) {
        logger.info("Updating product with id={}", product.getId());
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var productDAO = session.get(ProductDAO.class, product.getId());

            var ordDaoId = -1;
            if (product.getManufacturer() != null) {
                ordDaoId = updateOrganization(product.getManufacturer()).getId();
            } else {
                productDAO.setManufacturer(null);
            }
            productDAO.update(product);
            session.merge(productDAO);

            session.getTransaction().commit();
            logger.info("Product with id {} updated successfully.", product.getId());
            return ordDaoId;
        }
    }

    public OrganizationDAO updateOrganization(Organization organization) {
        logger.info("Updating organization with id={}", organization.getId());

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var organizationDAO = session.get(OrganizationDAO.class, organization.getId());
            organizationDAO.update(organization);

            session.merge(organizationDAO);
            session.getTransaction().commit();
            logger.info("Organization with id {} updated successfully.", organization.getId());
            return organizationDAO;
        }
    }

    public void clear(User user) {
        logger.info("Removing all products for user id={}.", user.id());

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createQuery("DELETE FROM products WHERE creator.id = :creator", Void.class);
            query.setParameter("creator", user.id());
            var deletedSize = query.executeUpdate();
            session.getTransaction().commit();
            logger.info("Cleared {} product(s).", deletedSize);
        }
    }

    public int remove(User user, int id) {
        logger.info("Removing product with id={} for user id={}.", id, user.id());

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var query = session.createQuery("DELETE FROM products WHERE creator.id = :creator AND id = :id", Void.class);
            query.setParameter("creator", user.id());
            query.setParameter("id", id);

            var deletedSize = query.executeUpdate();
            session.getTransaction().commit();
            logger.info("Removed {} product(s).", deletedSize);
            return deletedSize;
        }
    }

    public List<ProductDAO> loadProducts() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var cq = session.getCriteriaBuilder().createQuery(ProductDAO.class);
            var rootEntry = cq.from(ProductDAO.class);
            var all = cq.select(rootEntry);

            var result = session.createQuery(all).getResultList();
            session.getTransaction().commit();
            return result;
        }
    }
}
