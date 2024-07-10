package ru.clevertec.check.utils;

import java.util.List;

public class InputParams {

    private List<Item> products;
    private String discountCard;
    private double balanceDebitCard;


    public List<Item> getProducts() {
        return products;
    }

    public String getDiscountCard() {
        return discountCard;
    }

    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }


    public static InputParamsBuilder builder() {
        return new InputParamsBuilder();
    }

    public InputParams(List<Item> products,
                       String discountCard,
                       double balanceDebitCard) {
        this.products = products;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public InputParams() {

    }

    public void setProducts(List<Item> products) {
        this.products = products;
    }

    public void setDiscountCard(String discountCard) {
        this.discountCard = discountCard;
    }

    public void setBalanceDebitCard(double balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    public static class InputParamsBuilder {

        private List<Item> items;
        private String discountCard;
        private double balanceDebitCard;


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

        public InputParams build() {
            return new InputParams(items, discountCard, balanceDebitCard);
        }


    }
}
