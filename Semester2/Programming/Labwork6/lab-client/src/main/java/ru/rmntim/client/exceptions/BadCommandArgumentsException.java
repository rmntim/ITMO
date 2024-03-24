package ru.rmntim.client.exceptions;

public class BadCommandArgumentsException extends RuntimeException {
    public BadCommandArgumentsException(String s) {
        super(s);
    }
}
