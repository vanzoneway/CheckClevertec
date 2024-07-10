package ru.clevertec.check.dao;

import ru.clevertec.check.exception.NoSuchProductIdException;
import ru.clevertec.check.exception.ProductNotFoundException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.sql.*;
import java.util.List;


public class JdbcDiscountCardRepository implements DiscountCardRepository {

    DatabaseConnection databaseConnection;
    public JdbcDiscountCardRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public DiscountCard getDiscountCardByNumber(int number) throws SQLException {
        DiscountCard discountCard = new DiscountCard();

        Connection connection = databaseConnection.getConnection();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM discount_card WHERE number = " + number;

        ResultSet resultSet = statement.executeQuery(query);

        if(resultSet.next()) {
            discountCard.setNumber(resultSet.getInt("number"));
            discountCard.setAmount(resultSet.getInt("amount"));
            discountCard.setId(resultSet.getInt("id"));
        }

        statement.close();
        return discountCard;

    }

    @Override
    public DiscountCard getDiscountCardById(int discountCardId) throws SQLException, NoSuchProductIdException {
        DiscountCard discountCard = new DiscountCard();
        Connection connection = databaseConnection.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM discount_card WHERE id = " + discountCardId;
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            discountCard.setId(resultSet.getInt("id"));
            discountCard.setNumber(resultSet.getInt("number"));
            discountCard.setAmount(resultSet.getInt("amount"));
        }
        if (discountCard.getId() == null) throw new NoSuchProductIdException("NO SUCH DISCOUNT CARD");
        statement.close();

        return discountCard;
    }

    @Override
    public void updateDiscountCard(DiscountCard discountCard) throws Exception {
        Connection connection = databaseConnection.getConnection();

        String sql = "UPDATE discount_card " +
                "SET number = ?, amount = ? " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, discountCard.getNumber());
            preparedStatement.setInt(2, discountCard.getAmount());
            preparedStatement.setInt(3, discountCard.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0) {
                throw new NoSuchProductIdException("NO SUCH DISCOUNT CARD");
            }
        }
    }

    @Override
    public List<Product> getAllDiscountCard() throws Exception {
        return List.of();
    }

    @Override
    public void deleteDiscountCardById(int discountCardId) throws Exception {
        Connection connection = databaseConnection.getConnection();

        String deleteQuery = "DELETE FROM discount_card WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, discountCardId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new ProductNotFoundException("Discount card with ID " + discountCardId + " not found");
            }
        } catch (SQLException e) {
            throw new Exception("Error deleting discount card: " + e.getMessage(), e);
        }
    }

    @Override
    public void createDiscountCard(DiscountCard discountCard) throws Exception {
        Connection connection = databaseConnection.getConnection();

        String insertQuery = "INSERT INTO discount_card (number, amount) " +
                "VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        statement.setInt(1, discountCard.getNumber());
        statement.setInt(2, discountCard.getAmount());

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    discountCard.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("ERROR TO POST DISCOUNT CARD");
                }
            }
        }
        statement.close();
    }

}
