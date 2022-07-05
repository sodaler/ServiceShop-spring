package ru.ulstu.is.sbapp.exception;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(Long id) {
        super(String.format("Request with id [%d] is not found", id));
    }
}
