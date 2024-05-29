package server.managers;

import common.domain.Dragon;
import common.domain.DragonHead;
import common.user.User;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import server.App;
import server.dao.DragonDAO;
import server.dao.DragonHeadDAO;
import server.dao.UserDAO;

import java.util.List;

public class PersistenceManager {
    private final SessionFactory sessionFactory;
    private final Logger logger = App.logger;

    public PersistenceManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int add(User user, Dragon dragon) {
        logger.info("Adding new dragon {}", dragon.name());
        DragonHeadDAO newHead = null;
        if (dragon.head() != null) {
            newHead = addHead(user, dragon.head());
        }

        var dao = new DragonDAO(dragon);
        dao.setHead(newHead);
        dao.setCreator(new UserDAO(user));

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(dao);
            session.getTransaction().commit();
            logger.info("Product with id {} added successfully.", dao.getId());
            return dao.getId();
        }
    }

    public DragonHeadDAO addHead(User user, DragonHead head) {
        logger.info("Adding new head {}", head.eyesCount());

        var dao = new DragonHeadDAO(head);
        dao.setCreator(new UserDAO(user));

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(dao);
            session.getTransaction().commit();
            logger.info("Head with id {} added successfully.", dao.getId());
            return dao;
        }
    }

    public int update(Dragon dragon) {
        logger.info("Updating dragon with id={}", dragon.id());
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var dragonDao = session.get(DragonDAO.class, dragon.id());

            var headDaoId = -1;
            if (dragon.head() != null) {
                headDaoId = updateHead(dragon.head()).getId();
            } else {
                dragonDao.setHead(null);
            }
            dragonDao.update(dragon);
            session.merge(dragonDao);

            session.getTransaction().commit();
            logger.info("Product with id {} updated successfully.", dragon.id());
            return headDaoId;
        }
    }

    public DragonHeadDAO updateHead(DragonHead head) {
        logger.info("Updating head with id={}", head.getId());

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var headDAO = session.get(DragonHeadDAO.class, head.getId());
            headDAO.update(head);
            session.merge(headDAO);
            session.getTransaction().commit();
            logger.info("Head with id {} updated successfully.", head.getId());
            return headDAO;
        }
    }

    public void clear(User user) {
        logger.info("Removing all dragons for user id={}.", user.id());

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createMutationQuery("DELETE FROM dragons WHERE creator.id = :creator");
            query.setParameter("creator", user.id());
            var deletedSize = query.executeUpdate();
            session.getTransaction().commit();
            logger.info("Cleared {} product(s).", deletedSize);
        }
    }

    public int remove(User user, int id) {
        logger.info("Removing dragon with id={} for user id={}.", id, user.id());

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var query = session.createMutationQuery("DELETE FROM dragons WHERE creator.id = :creator AND id = :id");
            query.setParameter("creator", user.id());
            query.setParameter("id", id);

            var deletedSize = query.executeUpdate();
            session.getTransaction().commit();
            logger.info("Removed {} dragon(s).", deletedSize);
            return deletedSize;
        }
    }

    public List<DragonDAO> loadDragon() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var cq = session.getCriteriaBuilder().createQuery(DragonDAO.class);
            var rootEntry = cq.from(DragonDAO.class);
            var all = cq.select(rootEntry);

            var result = session.createQuery(all).getResultList();
            session.getTransaction().commit();
            return result;
        }
    }
}
