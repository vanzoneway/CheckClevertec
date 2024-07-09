package ru.clevertec.check;

import java.sql.SQLException;
import java.util.List;

public interface DiscountCardRepository {

    List<DiscountCard> getAllDiscountCards() throws SQLException;

    DiscountCard getDiscountCardByNumber(int number) throws SQLException;
}
