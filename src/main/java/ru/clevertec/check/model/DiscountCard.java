package ru.clevertec.check.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountCard {
    private Integer id;
    @JsonProperty("discountCard")
    private Integer number;
    @JsonProperty("discountAmount")
    private Integer amount;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
