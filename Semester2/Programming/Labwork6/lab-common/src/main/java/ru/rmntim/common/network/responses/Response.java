package ru.rmntim.common.network.responses;

import java.io.Serializable;

public record Response(String message, boolean isError) implements Serializable {
}
