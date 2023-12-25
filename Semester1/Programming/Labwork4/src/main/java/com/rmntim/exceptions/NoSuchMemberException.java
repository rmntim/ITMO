package com.rmntim.exceptions;

public class NoSuchMemberException extends Exception {
    public NoSuchMemberException(String name) {
        super("No member found with name: " + name);
    }
}
