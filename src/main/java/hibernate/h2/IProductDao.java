package hibernate.h2;

import java.util.List;

public interface IProductDao {

    Product findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    Product saveOrUpdate(Product product);



}
