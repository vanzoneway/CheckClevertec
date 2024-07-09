package ru.clevertec.check;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class JdbcDiscountCardRepository implements DiscountCardRepository {

    private final InputParams inputParams;

    public JdbcDiscountCardRepository(InputParams inputParams) {
        this.inputParams = inputParams;
    }


    @Override
    public List<DiscountCard> getAllDiscountCards() throws SQLException {

        List<DiscountCard> discountCards = new ArrayList<>();

        DiscountCard discountCard = new DiscountCard();
        Connection connection = DriverManager.getConnection(inputParams.getDatasourceUrl(),
                inputParams.getDatasourceUsername(), inputParams.getDatasourcePassword());

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM discount_card";
        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next()) {
            discountCard = new DiscountCard();
            discountCard.setId(resultSet.getInt("id"));
            discountCard.setAmount(resultSet.getInt("amount"));
            discountCard.setNumber(resultSet.getInt("number"));
            discountCards.add(discountCard);
        }

        connection.close();
        statement.close();


        return discountCards;
    }

    @Override
    public DiscountCard getDiscountCardByNumber(int number) throws SQLException {
        DiscountCard discountCard = new DiscountCard();

        Connection connection = DriverManager.getConnection(inputParams.getDatasourceUrl(),
                inputParams.getDatasourceUsername(), inputParams.getDatasourcePassword());

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM discount_card WHERE number = " + number;

        ResultSet resultSet = statement.executeQuery(query);

        if(resultSet.next()) {
            discountCard.setNumber(resultSet.getInt("number"));
            discountCard.setAmount(resultSet.getInt("amount"));
            discountCard.setId(resultSet.getInt("id"));
        }

        connection.close();
        statement.close();
        return discountCard;

    }
}
