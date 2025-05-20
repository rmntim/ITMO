package ru.rmntim.web.exceptions;

public class UserExistsException extends Exception {
    public UserExistsException(String message) {
        super(message);
    }
}
