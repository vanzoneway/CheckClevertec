package ru.clevertec.check.exception;

public class NotEnoughQuantityInStock extends Exception{
    public NotEnoughQuantityInStock(String message) {
        super(message);
    }
}
