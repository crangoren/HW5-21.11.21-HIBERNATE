package hibernate.h2;

import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

public class ProductDao implements IProductDao {
    private SessionFactoryUtils sessionFactoryUtils;

    public ProductDao(SessionFactoryUtils sessionFactoryUtils) {
        this.sessionFactoryUtils = sessionFactoryUtils;
    }

    public ProductDao() {
    }

    @Override
    public Product findById(Long id) {
        try (Session session = SessionFactoryUtils.getSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            session.getTransaction().commit();
            return  product;
        }
    }

    @Override
    public List<Product> findAll() {
        try (Session session = SessionFactoryUtils.getSession()) {
            session.beginTransaction();
            List<Product> products = session.createQuery("select p from Product p").getResultList();
            session.getTransaction().commit();
            return Collections.unmodifiableList(products);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = SessionFactoryUtils.getSession()) {
            session.beginTransaction();
            session.createQuery("delete Product where id = :id").setParameter("id", id).executeUpdate();
            session.getTransaction().commit();
        }
    }

    /*
    * в следующем методе не смог побороть регистр у title. если у нового продукта в тайтле регистр отличается от того что в бд,
    * метод его просто игнорирует
    */
    @Override
    public Product saveOrUpdate(Product product) {
        try (Session session = SessionFactoryUtils.getSession()) {
            session.beginTransaction();
            String newTitle = product.getTitle();
            int newPrice = product.getPrice();
            List<Product> products = session.createQuery("select p from Product p").getResultList();
            if (products.stream().anyMatch((p) -> p.getTitle().equalsIgnoreCase(newTitle))) {
                session.createQuery("update Product set price = :price where title = :title")
                        .setParameter("title", newTitle)
                        .setParameter("price", newPrice)
                        .executeUpdate();
            } else {
                session.save(new Product(newTitle, newPrice));
            }
            return product;
        }
    }
}
