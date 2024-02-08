package ru.rmntim.common.validation;

public interface Validator<T> {
    /**
     * @param entity Thing to be validated
     * @return {@code true} if the {@code entity} argument is valid, {@code false} otherwise
     */
    boolean validate(T entity);
}
