package ru.rmntim.cli.logic.parsers;

public interface Parser<T> {
    /**
     * @return parsed value
     * @throws ru.rmntim.cli.exceptions.BuildCancelledException if build was cancelled by user or by invalid script syntax
     */
    T parse();
}
