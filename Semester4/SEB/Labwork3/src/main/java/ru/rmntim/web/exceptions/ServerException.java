package ru.rmntim.web.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class ServerException extends WebApplicationException {
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

