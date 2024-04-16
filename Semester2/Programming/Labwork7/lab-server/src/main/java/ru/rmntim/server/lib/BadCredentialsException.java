package ru.rmntim.server.lib;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Bad credentials");
    }
}
