package hibernate.h2;


import org.hibernate.Session;

public class MainApp {

    public static void main(String[] args) {
        SessionFactoryUtils sessionFactoryUtils = new SessionFactoryUtils();
        sessionFactoryUtils.init();

        try {
            ProductDao productDao = new ProductDao(sessionFactoryUtils);

            System.out.println(productDao.findAll().toString());
            productDao.deleteById(2L);
            System.out.println(productDao.findAll().toString());
            productDao.saveOrUpdate(new Product("Milk", 59));
            System.out.println(productDao.findAll().toString());
            productDao.saveOrUpdate(new Product("Soda", 44));
            System.out.println(productDao.findAll().toString());

//            System.out.println(productDao.findAll().toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactoryUtils.shutdown();
        }

    }
}
