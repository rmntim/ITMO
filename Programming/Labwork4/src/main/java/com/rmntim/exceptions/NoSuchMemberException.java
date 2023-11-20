package com.rmntim.exceptions;

public class NoSuchMemberException extends RuntimeException {
    public NoSuchMemberException(String name) {
        super("No member found with name: " + name);
    }
}
