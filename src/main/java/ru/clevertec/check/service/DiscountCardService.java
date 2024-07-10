package ru.clevertec.check.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.check.dao.DatabaseConnection;
import ru.clevertec.check.dao.DiscountCardRepository;
import ru.clevertec.check.dao.JdbcDiscountCardRepository;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class DiscountCardService {

    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private final DiscountCardRepository discountCardRepository
            = new JdbcDiscountCardRepository(databaseConnection);




    public DiscountCardService() throws SQLException, ClassNotFoundException {

    }

    public void returnDiscountCard(int discountCardId,
                                   HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(discountCardRepository
                .getDiscountCardById(discountCardId)));

    }

    public void postDiscountCard(HttpServletRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DiscountCard discountCard = objectMapper.readValue(request.getInputStream(), DiscountCard.class);
        discountCardRepository.createDiscountCard(discountCard);
    }


    public void updateDiscountCard(int discountCardId, HttpServletRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DiscountCard discountCard = objectMapper.readValue(request.getInputStream(), DiscountCard.class);
        discountCard.setId(discountCardId);
        discountCardRepository.updateDiscountCard(discountCard);
    }


    public void deleteDiscountCard(int discountCardId) throws Exception {
        discountCardRepository.deleteDiscountCardById(discountCardId);
    }





}
