package server.repositories;

import common.domain.Address;
import common.domain.Coordinates;
import common.domain.Organization;
import common.domain.Product;
import common.exceptions.BadOwnerException;
import common.user.User;
import common.utility.ProductComparator;
import org.slf4j.Logger;
import server.App;
import server.managers.PersistenceManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProductRepository {
    private final Logger logger = App.logger;

    private Queue<Product> collection = new PriorityQueue<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final PersistenceManager persistenceManager;

    public ProductRepository(PersistenceManager persistenceManager) {
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
        for (var product : new ArrayList<>(get())) {
            if (!product.validate()) {
                logger.warn("Product with id={} is invalid.", product.getId());
                return false;
            }
            if (product.getManufacturer() != null) {
                if (!product.getManufacturer().validate()) {
                    logger.warn("Manufacturer with id={} is invalid.", product.getId());
                    return false;
                }
            }
        }
        logger.info("All products are valid.");
        return true;
    }

    public Queue<Product> get() {
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

    public Product first() {
        if (collection.isEmpty()) return null;
        return sorted().get(0);
    }

    public List<Product> sorted() {
        return new ArrayList<>(collection)
                .stream()
                .sorted(new ProductComparator())
                .collect(Collectors.toList());
    }

    public Product getById(int id) {
        for (Product element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkExist(int id) {
        return getById(id) != null;
    }

    public synchronized int add(User user, Product element) {
        var newId = persistenceManager.add(user, element);
        collection.add(element.copy(newId, user.withoutPassword()));
        lastSaveTime = LocalDateTime.now();
        logger.info("New product added successfully.");
        return newId;
    }

    public synchronized void update(User user, Product element) throws BadOwnerException {
        var product = getById(element.getId());
        if (product == null) {
            add(user, element);
        } else if (product.getCreatorId() == user.getId()) {
            logger.info("Updating product with id={}.", product.getId());

            var orgId = persistenceManager.update(user, element);

            if (orgId != -1) element.getManufacturer().setId(orgId);
            getById(element.getId()).update(element);
            lastSaveTime = LocalDateTime.now();

            logger.info("Product added successfully");
        } else {
            logger.warn("Wrong owner for product with id={}.", product.getId());
            throw new BadOwnerException();
        }
    }

    public synchronized int remove(User user, int id) throws BadOwnerException {
        if (getById(id).getCreatorId() != user.getId()) {
            logger.warn("Wrong owner for product with id={}.", id);
            throw new BadOwnerException();
        }

        var removedCount = persistenceManager.remove(user, id);
        if (removedCount == 0) {
            logger.warn("No elements were removed");
            return 0;
        }

        collection.removeIf(product -> product.getId() == id && product.getCreatorId() == user.getId());
        lastSaveTime = LocalDateTime.now();

        return removedCount;
    }

    public synchronized void clear(User user) {
        persistenceManager.clear(user);

        collection.removeIf(product -> product.getCreatorId() == user.getId());
        lastSaveTime = LocalDateTime.now();
    }

    private synchronized void load() {
        logger.info("Loading data");

        collection = new PriorityQueue<>();
        var daos = persistenceManager.loadProducts();

        var products = daos.stream().map((dao) -> {
            Organization manufacturer = null;
            if (dao.getManufacturer() != null) {
                manufacturer = new Organization(
                        dao.getManufacturer().getId(),
                        dao.getManufacturer().getName(),
                        dao.getManufacturer().getEmployeesCount(),
                        dao.getManufacturer().getType(),
                        new Address(dao.getManufacturer().getStreet(), dao.getManufacturer().getZipCode())
                );
            }
            return new Product(
                    dao.getId(),
                    dao.getName(),
                    new Coordinates(dao.getX(), dao.getY()),
                    dao.getCreationDate(),
                    dao.getPrice(),
                    dao.getPartNumber(),
                    dao.getUnitOfMeasure(),
                    manufacturer,
                    new User(dao.getCreator().getId(), dao.getCreator().getName(), "")
            );
        }).toList();

        collection.addAll(products);
        lastInitTime = LocalDateTime.now();

        logger.info("Data loaded");
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Collection is empty!";

        var info = new StringJoiner("\n\n");
        for (Product product : collection) {
            info.add(product.toString());
        }
        return info.toString();
    }
}
