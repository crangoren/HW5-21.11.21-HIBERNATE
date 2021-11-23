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
                    //.equalsIgnoreCase(newTitle)).findFirst().orElse(session.save(new Product(newTitle)));

//            String newTitle = product.getTitle();
//            int newPrice = product.getPrice();
//            Product oldProduct = (Product) session
//            .createQuery("select p from Product p where title = :title")
//            .setParameter("title", newTitle)
//            .getSingleResult();
//            System.out.println(oldProduct);
//            if (oldProduct.getTitle().equalsIgnoreCase(product.getTitle())) {
//                session.createQuery("update Product set price = :price where title = :title")
//                        .setParameter("title", newTitle)
//                        .setParameter("price", newPrice)
//                        .executeUpdate();
//            }
//            session.save(new Product(newTitle, newPrice));
//            session.getTransaction().commit();
//            if (oldProduct.getPrice() == 0) {
//                session.createQuery("insert Product set title = :title")
//                        .setParameter("title", newTitle)
//                        .setParameter("price", newPrice)
//                        .executeUpdate();
                //session.save(product);
//            }
//                Product newProduct = new Product();
//                newProduct.setTitle(newTitle);
//                newProduct.setPrice(newPrice);
//                session.save(newProduct);
//                session.getTransaction().commit();
//                   }
            return product;
        }
    }

}
