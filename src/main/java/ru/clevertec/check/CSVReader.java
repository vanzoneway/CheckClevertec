package main.java.ru.clevertec.check;

import java.util.List;

public interface CSVReader {

    List<String[]> readCSV(String fileName) throws IllegalArgumentException;

    String[] findProductById(List<String[]> csvData, int productId);

    String[] findDiscountInfoByCardNumber(List<String[]> csvData, String cardNumber);

}
