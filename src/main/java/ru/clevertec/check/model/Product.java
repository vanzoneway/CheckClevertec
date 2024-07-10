package ru.clevertec.check.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;



public class Product {
    private Integer id;
    private String description;
    private Double price;

    @JsonProperty("quantity")
    private Integer quantityInStock;

    @JsonProperty("isWholesale")
    private Boolean isWholesaleProduct;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Boolean getIsWholesaleProduct() {
        return isWholesaleProduct;
    }

    public void setIsWholesaleProduct(Boolean isWholesaleProduct) {
        this.isWholesaleProduct = isWholesaleProduct;
    }
}
