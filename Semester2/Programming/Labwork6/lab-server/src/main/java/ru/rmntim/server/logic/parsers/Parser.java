package ru.rmntim.server.logic.parsers;

/**
 * @param <T> type of the object to parse
 */
public interface Parser<T> {
    /**
     * @return parsed value
     * @throws ru.rmntim.server.exceptions.BuildCancelledException if build was cancelled by user or by invalid script syntax
     */
    T parse();
}
