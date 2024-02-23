package ru.rmntim.cli.models.builders;

public interface Builder<T> {
    T build() throws InvalidBuildException;
}
