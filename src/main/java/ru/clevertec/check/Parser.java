package main.java.ru.clevertec.check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static InputParams parseBasicCommandArgs(String[] args) throws IOException {
        List<Item> items = new ArrayList<>();
        String discountCard = null;
        double balanceDebitCard = 0.0;
        String pathToFile = null;
        String saveToFile = null;

        try {
            for (String arg : args) {
                if (arg.contains("-")) {
                    String[] parts = arg.split("-");
                    int id = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    items.add(new Item(id, quantity));
                } else if (arg.contains("=")) {
                    String[] parts = arg.split("=");
                    String key = parts[0];
                    String value = parts[1];
                    switch (key) {
                        case "discountCard" -> discountCard = value;
                        case "balanceDebitCard" -> balanceDebitCard = Double.parseDouble(value);
                        case "pathToFile" -> pathToFile = value;
                        case "saveToFile" -> saveToFile = value;
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("BAD REQUEST");
        }

        return InputParams
                .builder()
                .items(items)
                .balanceDebitCard(balanceDebitCard)
                .discountCard(discountCard)
                .pathToFile(pathToFile)
                .saveToFile(saveToFile)
                .build();
    }

}
