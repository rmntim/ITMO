package ru.rmntim.common.network;

import java.io.Serializable;

public record Response(Status status, String message) implements Serializable {
    public enum Status {
        OK,
        ERROR,
    }

    public Response(String message) {
        this(Status.OK, message);
    }
}
