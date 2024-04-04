package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

import java.io.Serializable;

/**
 * Base class for all commands.
 */
public abstract class Command implements Serializable {
    /**
     * Accepts visitor and returns response.
     *
     * @param visitor command visitor
     * @return response
     */
    public abstract Response accept(Visitor visitor);

    public interface Visitor {
        Response visit(Info info);
    }
}
