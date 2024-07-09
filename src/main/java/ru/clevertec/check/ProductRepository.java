package ru.clevertec.check;


import java.sql.SQLException;

public interface ProductRepository {

    Product getProductById(int productId) throws SQLException;

}
