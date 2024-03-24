package ru.rmntim.server.validators;

import ru.rmntim.server.exceptions.ValidationException;

public interface Validator<T> {
    /**
     * @param value value to validate
     * @throws ValidationException  if value is not valid
     * @throws NullPointerException if value is {@code null}
     */
    void validate(T value) throws ValidationException;
}
