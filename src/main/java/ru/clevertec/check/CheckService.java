package ru.clevertec.check;


import com.sun.jdi.InternalException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckService {

    private final String pathToResult;

    private final CSVWorker CSVWorker = new CSVWorker();


    private double total = 0.0;
    private double discount = 0.0;

    private final ProductRepository productRepository;
    private final DiscountCardRepository discountCardRepository;


    private final InputParams inputParams;

    public CheckService(InputParams inputParams,
                        DiscountCardRepository discountCardRepository,
                        ProductRepository productRepository) {

        this.inputParams = inputParams;
        this.pathToResult = inputParams.getSaveToFile();
        this.productRepository = productRepository;
        this.discountCardRepository = discountCardRepository;

    }


    public void setTotal(double total) {
        this.total = total;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    public void createCheck() throws Exception {


        List<String[]> csvResult = new ArrayList<>();

        if (Objects.isNull(inputParams.getItems()) || !isStringContainsOnlyDigits(inputParams.getDiscountCard()))
            throw new InternalException("BAD REQUEST");

        if (Objects.isNull(pathToResult))
            throw new IllegalArgumentException("BAD REQUEST");

        addDateAndTime(csvResult);
        addOrderInfo(csvResult);

        if (inputParams.getBalanceDebitCard() < total - discount)
            throw new IllegalArgumentException("NOT ENOUGH MONEY");

        if (Objects.nonNull(inputParams.getDiscountCard())) {
            addDiscountCardInfo(csvResult);
        }

        addTotalInfo(csvResult);

        CSVWorker.writeCSV(pathToResult, csvResult);
        CSVWorker.writeCSVToConsole(csvResult);


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

    private void addOrderInfo(List<String[]> csvResult) throws IllegalArgumentException, SQLException {
        String[] indexes = {"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        csvResult.add(indexes);


        List<Item> items = inputParams.getItems();

        for (Item item : items) {
            Product product = productRepository.getProductById(item.getId());
            if (Objects.isNull(product)) throw new IllegalArgumentException("BAD REQUEST");
            String qty = String.valueOf(item.getQuantity());
            String description = product.getDescription();
            String price = String.valueOf(product.getPrice());
            double row_total = item.getQuantity() * product.getPrice();
            total += row_total;
            String total = String.valueOf(row_total);
            String discount =
                    countDiscountForOneRow(
                            inputParams.getDiscountCard(),
                            qty,
                            total,
                            product.isIs_wholesale_product());
            this.discount += Double.parseDouble(discount);

            csvResult.add(new String[]{qty, description, price + "$", discount + "$", total + "$"});
        }
        csvResult.add(new String[]{});

    }

    private String countDiscountForOneRow(String discountCardNumber,
                                          String qty,
                                          String total,
                                          boolean wholesale) throws SQLException {

        if (Integer.parseInt(qty) >= 5 && wholesale) {
            return String.format("%.2f", Double.parseDouble(total) * 0.1);
        } else if (discountCardNumber == null) {
            return "0";
        } else {
            return countDiscountIfCardExists(discountCardNumber, total);
        }

    }

    private String countDiscountIfCardExists(String discountCardNumber,
                                             String total) throws SQLException {
        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(Integer.parseInt(discountCardNumber));
        if (discountCard== null) {
            return String.format("%.2f", Double.parseDouble(total) * 0.03);
        }
        return String.format("%.2f", Double.parseDouble(total) * discountCard.getAmount() * 0.01);

    }

    private void addDiscountCardInfo(List<String[]> csvResult) throws SQLException {
        String[] indexes = {"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        csvResult.add(indexes);

        String discountCardNumber = inputParams.getDiscountCard();
        String[] discountInfo = {discountCardNumber, getDiscountPercentageIfCardExists(discountCardNumber) + "$"};

        csvResult.add(discountInfo);
        csvResult.add(new String[]{});
    }

    private String getDiscountPercentageIfCardExists(String discountCardNumber) throws SQLException {
        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(Integer.parseInt(discountCardNumber));
        if (discountCard == null) {
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


}
