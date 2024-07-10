package ru.clevertec.check.dao;

import ru.clevertec.check.exception.NoSuchProductIdException;
import ru.clevertec.check.exception.ProductNotFoundException;
import ru.clevertec.check.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductRepository implements ProductRepository {

    DatabaseConnection databaseConnection;
    public JdbcProductRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Product getProductById(int productId) throws SQLException, ClassNotFoundException {

        Product product = new Product();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM product WHERE id = " + productId;
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            product.setId(resultSet.getInt("id"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice(resultSet.getDouble("price"));
            product.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
            product.setIsWholesaleProduct(resultSet.getBoolean("is_wholesale_product"));
        }
        statement.close();

        return product;
    }


    @Override
    public void updateProduct(Product product) throws SQLException, NoSuchProductIdException, ClassNotFoundException {

        Connection connection = databaseConnection.getConnection();

        String sql = "UPDATE product " +
                "SET description = ?, price = ?, quantity_in_stock = ?, is_wholesale_product = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getDescription());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantityInStock());
            statement.setBoolean(4, product.getIsWholesaleProduct());
            statement.setInt(5, product.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new NoSuchProductIdException("Product with ID " + product.getId() + " not found");
            }
        }
    }

    @Override
    public List<Product> getAllProducts() throws SQLException{
        Connection connection = databaseConnection.getConnection();

        List<Product> products = new ArrayList<>();


        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM product");

            while (resultSet.next()) {

                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                product.setIsWholesaleProduct(resultSet.getBoolean("is_wholesale_product"));

                products.add(product);
            }
        }

        return products;
    }

    @Override
    public void deleteProductById(int productId) throws SQLException, ClassNotFoundException, ProductNotFoundException {
        Connection connection = databaseConnection.getConnection();

        String deleteQuery = "DELETE FROM product WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, productId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new ProductNotFoundException("NO SUCH PRODUCT ID");
            }
        }

    }



}
