package ru.clevertec.check.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.check.dao.*;
import ru.clevertec.check.exception.*;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.utils.CSVWorker;

import ru.clevertec.check.utils.InputParams;
import ru.clevertec.check.utils.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckService {


    private double total;
    private double discount;



    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    ProductRepository productRepository = new JdbcProductRepository(databaseConnection);
    DiscountCardRepository discountCardRepository = new JdbcDiscountCardRepository(databaseConnection);
    CSVWorker csvWorker = new CSVWorker();

    public CheckService() throws SQLException, ClassNotFoundException {
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    public void createCheck(HttpServletResponse response, HttpServletRequest request) throws Exception {


        total = 0;
        discount = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"check.csv\"");
        InputParams inputParams = objectMapper.readValue(request.getInputStream(), InputParams.class);

        List<String[]> csvResult = new ArrayList<>();

        if (Objects.isNull(inputParams.getProducts()) || !isStringContainsOnlyDigits(inputParams.getDiscountCard()))
            throw new NoValidParametersException("BAD REQUEST");

        addDateAndTime(csvResult);
        addOrderInfo(csvResult, inputParams);

        System.out.println(inputParams.getBalanceDebitCard());
        if (inputParams.getBalanceDebitCard() < total - discount)
            throw new NotEnoughMoneyException("NOT ENOUGH MONEY");

        if (Objects.nonNull(inputParams.getDiscountCard())) {
            addDiscountCardInfo(csvResult, inputParams);
        }

        addTotalInfo(csvResult);

        sendCSVResponse(response, csvResult);
        csvWorker.writeCSVToConsole(csvResult);

    }

    private boolean isStringContainsOnlyDigits(String word) {
        if (Objects.isNull(word)) return false;
        boolean isOnlyDigits = true;
        for (int i = 0; i < word.length() && isOnlyDigits; i++) {
            if (!Character.isDigit(word.charAt(i))) {
                isOnlyDigits = false;
            }
        }
        return isOnlyDigits;
    }

    private void addDateAndTime(List<String[]> csvResult) {
        String[] indexes = {"DATE", "TIME"};
        csvResult.add(indexes);

        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy.MM.yyyy");
        String date = currentDateTime.format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = currentDateTime.format(timeFormatter);

        csvResult.add(new String[]{date, time});
        csvResult.add(new String[]{});


    }


    private void addOrderInfo(List<String[]> csvResult, InputParams inputParams) throws Exception {

        String[] indexes = {"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        csvResult.add(indexes);


        List<Item> items = inputParams.getProducts();

        for (Item item : items) {
            Product product = productRepository.getProductById(item.getId());

            if (Objects.equals(product.getId(), null))
                throw new ProductNotFoundException("BAD REQUEST");

            String qty = String.valueOf(item.getQuantity());

            if(product.getQuantityInStock() < Integer.parseInt(qty))
                throw new NotEnoughQuantityInStock("NOT ENOUGH QUANTITY IN STOCK");

            String description = product.getDescription();
            String price = String.valueOf(product.getPrice());
            double row_total = item.getQuantity() * product.getPrice();
            total += row_total;
            String total = String.format("%.2f", row_total);
            String discount =
                    countDiscountForOneRow(
                            inputParams.getDiscountCard(),
                            qty,
                            total,
                            product.getIsWholesaleProduct());
            this.discount += Double.parseDouble(discount);

            csvResult.add(new String[]{qty, description, price + "$", discount + "$", total + "$"});
            product.setQuantityInStock(product.getQuantityInStock() - Integer.parseInt(qty));
            productRepository.updateProduct(product);
        }
        csvResult.add(new String[]{});

    }

    private String countDiscountForOneRow(String discountCardNumber,
                                          String qty,
                                          String total,
                                          boolean wholesale) throws SQLException {

        if (Integer.parseInt(qty) >= 5 && wholesale)
            return String.format("%.2f", Double.parseDouble(total) * 0.1);

        if (discountCardNumber == null)
            return "0";

        return countDiscountIfCardExists(discountCardNumber, total);


    }

    private String countDiscountIfCardExists(String discountCardNumber,
                                             String total) throws SQLException {
        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(Integer.parseInt(discountCardNumber));
        if (discountCard.getId() == null)
            return String.format("%.2f", Double.parseDouble(total) * 0.03);

        return String.format("%.2f", Double.parseDouble(total) * discountCard.getAmount() * 0.01);

    }

    private void addDiscountCardInfo(List<String[]> csvResult, InputParams inputParams) throws SQLException {
        String[] indexes = {"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        csvResult.add(indexes);

        String discountCardNumber = inputParams.getDiscountCard();
        String[] discountInfo = {discountCardNumber, getDiscountPercentageIfCardExists(discountCardNumber) + "$"};

        csvResult.add(discountInfo);
        csvResult.add(new String[]{});
    }

    private String getDiscountPercentageIfCardExists(String discountCardNumber) throws SQLException {
        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(Integer.parseInt(discountCardNumber));
        if (discountCard.getId() == null) {
            return "3";
        }
        return String.valueOf(discountCard.getAmount());

    }

    private void addTotalInfo(List<String[]> csvResult) {
        String[] indexes = {"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};
        csvResult.add(indexes);

        String[] totalInfo = {
                String.format("%.2f", total) + "$",
                String.format("%.2f", discount) + "$",
                String.format("%.2f", total - discount) + "$"
        };
        csvResult.add(totalInfo);
        csvResult.add(new String[]{});
    }

    private void sendCSVResponse(HttpServletResponse response, List<String[]> csvResult) throws IOException {
        PrintWriter writer = response.getWriter();

        for (String[] row : csvResult)
            writer.println(String.join(";", row));

    }



}
