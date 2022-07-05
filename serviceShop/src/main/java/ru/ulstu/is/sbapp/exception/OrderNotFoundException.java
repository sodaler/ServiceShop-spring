package ru.ulstu.is.sbapp.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id) {
        super(String.format("Order with id [%d] is not found", id));
    }
}
