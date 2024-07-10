package ru.clevertec.check.dao;


import ru.clevertec.check.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {

    Product getProductById(int productId) throws Exception;
    void updateProduct(Product product) throws Exception;
    List<Product> getAllProducts() throws Exception;
    void deleteProductById(int productId) throws Exception;

}
