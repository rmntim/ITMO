package ru.rmntim.server;

import ru.rmntim.server.models.Dragon;

import java.util.TreeSet;

public final class Server {
    private final TreeSet<Dragon> collection = new TreeSet<>();

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {

    }
}
