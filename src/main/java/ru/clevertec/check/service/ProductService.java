package ru.clevertec.check.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.check.dao.DatabaseConnection;
import ru.clevertec.check.dao.JdbcProductRepository;
import ru.clevertec.check.dao.ProductRepository;
import ru.clevertec.check.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class ProductService{

    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private final ProductRepository productRepository = new JdbcProductRepository(databaseConnection);

    public ProductService() throws SQLException, ClassNotFoundException {
    }

    public void returnProduct(int productId, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(productRepository.getProductById(productId)));
    }

    public void returnAllProducts(HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(productRepository.getAllProducts()));

    }

    public void updateProduct(int productId, HttpServletRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(request.getInputStream(), Product.class);
        product.setId(productId);
        productRepository.updateProduct(product);
    }


    public void deleteProduct(int productId) throws Exception {
        productRepository.deleteProductById(productId);
    }
}
