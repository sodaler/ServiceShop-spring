package ru.ulstu.is.sbapp.exception;

public class RequestTypeNotFoundException extends RuntimeException{
    public RequestTypeNotFoundException(Long id) {
        super(String.format("Ordering with id [%d] is not found", id));
    }
}
