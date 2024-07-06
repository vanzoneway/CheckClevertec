package main.java.ru.clevertec.check;

import java.util.List;

public class InputParams {

    private final List<Item> items;
    private final String discountCard;
    private final double balanceDebitCard;
    private final String pathToFile;
    private final String saveToFile;


    public List<Item> getItems() {
        return items;
    }

    public String getDiscountCard() {
        return discountCard;
    }

    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public String getSaveToFile() {
        return saveToFile;
    }

    public static InputParamsBuilder builder() {
        return new InputParamsBuilder();
    }

    public InputParams(List<Item> items,
                       String discountCard,
                       double balanceDebitCard,
                       String pathToFile,
                       String saveToFile) {
        this.items = items;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
        this.pathToFile = pathToFile;
        this.saveToFile = saveToFile;
    }


    public static class InputParamsBuilder {

        private List<Item> items;
        private String discountCard;
        private double balanceDebitCard;
        private String pathToFile;
        private String saveToFile;


        public InputParamsBuilder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public InputParamsBuilder discountCard(String discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public InputParamsBuilder balanceDebitCard(double balanceDebitCard) {
            this.balanceDebitCard = balanceDebitCard;
            return this;
        }

        public InputParamsBuilder pathToFile(String pathToFile) {
            this.pathToFile = pathToFile;
            return this;
        }

        public InputParamsBuilder saveToFile(String saveToFile) {
            this.saveToFile = saveToFile;
            return this;
        }

        public InputParams build() {
            return new InputParams(items, discountCard, balanceDebitCard, pathToFile, saveToFile);
        }
    }
}
