package ru.clevertec.check.dao;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.sql.SQLException;
import java.util.List;


public interface DiscountCardRepository {

    DiscountCard getDiscountCardByNumber(int number) throws SQLException;

    DiscountCard getDiscountCardById(int discountCardId) throws Exception;
    void updateDiscountCard(DiscountCard discountCard) throws Exception;
    List<Product> getAllDiscountCard() throws Exception;
    void deleteDiscountCardById(int discountCardId) throws Exception;
    void createDiscountCard(DiscountCard discountCard) throws Exception;
}
