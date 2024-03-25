package ru.rmntim.common.network.responses;

import java.io.Serializable;

public record Response(String message, String error) implements Serializable {
}
