package ru.clevertec.check;

import java.util.List;

public class InputParams {

    private final List<Item> items;
    private final String discountCard;
    private final double balanceDebitCard;
    private final String saveToFile;
    private final String datasourceUrl;
    private final String datasourceUsername;
    private final String datasourcePassword;


    public List<Item> getItems() {
        return items;
    }

    public String getDiscountCard() {
        return discountCard;
    }

    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public String getSaveToFile() {
        return saveToFile;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public static InputParamsBuilder builder() {
        return new InputParamsBuilder();
    }

    public InputParams(List<Item> items,
                       String discountCard,
                       double balanceDebitCard,
                       String saveToFile,
                       String datasourceUrl,
                       String datasourceUsername,
                       String datasourcePassword) {
        this.items = items;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
        this.saveToFile = saveToFile;
        this.datasourceUrl = datasourceUrl;
        this.datasourceUsername = datasourceUsername;
        this.datasourcePassword = datasourcePassword;
    }


    public static class InputParamsBuilder {

        private List<Item> items;
        private String discountCard;
        private double balanceDebitCard;
        private String saveToFile;
        private String datasourceUrl;
        private String datasourceUsername;
        private String datasourcePassword;


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


        public InputParamsBuilder saveToFile(String saveToFile) {
            this.saveToFile = saveToFile;
            return this;
        }

        public InputParamsBuilder datasourceUrl(String datasourceUrl) {
            this.datasourceUrl = datasourceUrl;
            return this;
        }

        public InputParamsBuilder datasourceUsername(String datasourceUsername) {
            this.datasourceUsername = datasourceUsername;
            return this;
        }

        public InputParamsBuilder datasourcePassword(String datasourcePassword) {
            this.datasourcePassword = datasourcePassword;
            return this;
        }

        public InputParams build() {
            return new InputParams(items, discountCard, balanceDebitCard,
                    saveToFile, datasourceUrl, datasourceUsername,
                    datasourcePassword);
        }


    }
}
