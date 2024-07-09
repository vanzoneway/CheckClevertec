package ru.clevertec.check;

import java.sql.*;

public class JdbcProductRepository implements ProductRepository {

    private final InputParams inputParams;

    public JdbcProductRepository(InputParams inputParams) {
        this.inputParams = inputParams;
    }

    @Override
    public Product getProductById(int productId) throws SQLException {

        Product product = new Product();
        Connection connection = DriverManager.getConnection(inputParams.getDatasourceUrl(),
                inputParams.getDatasourceUsername(), inputParams.getDatasourcePassword());
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM product WHERE id = " + productId;
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            product.setId(resultSet.getInt("id"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice(resultSet.getDouble("price"));
            product.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
            product.setIs_wholesale_product(resultSet.getBoolean("is_wholesale_product"));
        }
        statement.close();
        connection.close();

        return product;
    }
}
