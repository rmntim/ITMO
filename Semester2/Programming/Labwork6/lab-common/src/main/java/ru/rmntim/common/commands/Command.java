package ru.rmntim.common.commands;

import ru.rmntim.common.CollectionManager;
import ru.rmntim.common.network.Response;

public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Command parse(byte[] rawCommand) {
        return null;
    }

    public abstract Response execute(CollectionManager collectionManager);
}
