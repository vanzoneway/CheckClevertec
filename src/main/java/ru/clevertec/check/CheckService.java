package main.java.ru.clevertec.check;


import com.sun.jdi.InternalException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckService {

    private final String pathToResult;

    private final CSVWorker CSVWorker = new CSVWorker();

    List<String[]> csvProducts;
    List<String[]> csvDiscounts;

    private double total = 0.0;
    private double discount = 0.0;

    InputParams inputParams;

    public CheckService(String[] args,
                        String pathToProducts,
                        String pathToDiscountCards,
                        String pathToResult) throws IOException {

        this.pathToResult = pathToResult;
        this.csvProducts = CSVWorker.readCSV(pathToProducts);
        this.csvDiscounts = CSVWorker.readCSV(pathToDiscountCards);
        this.inputParams = Parser.parseBasicCommandArgs(args);
    }


    public void createCheck() throws Exception {


        List<String[]> csvResult = new ArrayList<>();

        if (Objects.isNull(inputParams.getItems()) || !isStringContainsOnlyDigits(inputParams.getDiscountCard()))
            throw new InternalException("BAD REQUEST");


        addDateAndTime(csvResult);
        addOrderInfo(csvResult);

        if(inputParams.getBalanceDebitCard() < total - discount)
            throw new IllegalArgumentException("NOT ENOUGH MONEY");

        if (Objects.nonNull(inputParams.getDiscountCard())) {
            addDiscountCardInfo(csvResult);
        }

        addTotalInfo(csvResult);

        CSVWorker.writeCSV(pathToResult, csvResult);


    }

    private boolean isStringContainsOnlyDigits(String word) {
        if(Objects.isNull(word)) return false;
        boolean isOnlyDigits = true;
        for(int i = 0; i < word.length() && isOnlyDigits; i++) {
            if(!Character.isDigit(word.charAt(i))) {
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

    private void addOrderInfo(List<String[]> csvResult) throws IllegalArgumentException {
        String[] indexes = {"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        csvResult.add(indexes);


        List<Item> items = inputParams.getItems();

        for(Item item : items) {
            String[] product = CSVWorker.findProductById(csvProducts, item.getId());
            if (Objects.isNull(product)) throw new IllegalArgumentException("BAD REQUEST");
            String qty = String.valueOf(item.getQuantity());
            String description = product[1];
            String price = product[2];
            double row_total = item.getQuantity() * Double.parseDouble(product[2]);
            total += row_total;
            String total = String.valueOf(row_total);
            String discount =
                    countDiscountForOneRow(
                            inputParams.getDiscountCard(),
                            qty,
                            total,
                            product[4]);
            this.discount += Double.parseDouble(discount);

            csvResult.add(new String[]{qty, description, price + "$", discount + "$", total + "$"});
        }
        csvResult.add(new String[]{});

    }

    private String countDiscountForOneRow(String discountCardNumber,
                                          String qty,
                                          String total,
                                          String wholesale) {

        if (Integer.parseInt(qty) >= 5 && Objects.equals(wholesale, "+")) {
            return String.format("%.2f", Double.parseDouble(total) * 0.1);
        } else if (discountCardNumber == null) {
            return "0";
        } else {
            return countDiscountIfCardExists(discountCardNumber, total);
        }

    }

    private String countDiscountIfCardExists(String discountCardNumber,
                                             String total) {
        String[] discountInfo = CSVWorker.findDiscountInfoByCardNumber(csvDiscounts, discountCardNumber);
        if(discountInfo == null) {
            return String.format("%.2f",Double.parseDouble(total) * 0.03);
        }
        return String.format("%.2f",Double.parseDouble(total) * Double.parseDouble(discountInfo[2]) * 0.01);

    }

    private void addDiscountCardInfo(List<String[]> csvResult) {
        String[] indexes = {"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        csvResult.add(indexes);

        String discountCardNumber = inputParams.getDiscountCard();
        String[] discountInfo = {discountCardNumber, getDiscountPercentageIfCardExists(discountCardNumber) + "$"};

        csvResult.add(discountInfo);
        csvResult.add(new String[]{});
    }

    private String getDiscountPercentageIfCardExists(String discountCardNumber) {
        String[] discountInfo = CSVWorker.findDiscountInfoByCardNumber(csvDiscounts, discountCardNumber);
        if(discountInfo == null) {
            return "3";
        }
        return discountInfo[2];

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
