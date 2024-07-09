package ru.clevertec.check;

public class Product {
    private int id;
    private String description;
    private double price;
    private int quantity_in_stock;
    private boolean is_wholesale_product;

    public int getId() {
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

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    public boolean isIs_wholesale_product() {
        return is_wholesale_product;
    }

    public void setIs_wholesale_product(boolean is_wholesale_product) {
        this.is_wholesale_product = is_wholesale_product;
    }
}
