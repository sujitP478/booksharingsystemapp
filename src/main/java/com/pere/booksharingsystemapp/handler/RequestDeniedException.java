package com.pere.booksharingsystemapp.handler;

public class RequestDeniedException extends RuntimeException{
    public RequestDeniedException(String message) {
        super(message);
    }
}
