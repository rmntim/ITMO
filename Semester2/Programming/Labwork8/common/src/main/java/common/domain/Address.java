package common.domain;

import java.io.Serializable;

/**
 * @param street  Строка не может быть пустой, Поле не может быть null
 * @param zipCode Длина строки должна быть не меньше 6, Поле может быть null
 */
public record Address(String street, String zipCode) implements Serializable {
    public boolean validate() {
        if (street == null || street.isEmpty()) return false;
        return zipCode == null || zipCode.length() >= 6;
    }

    @Override
    public String toString() {
        return "ул. " + street + (zipCode == null ? "" : ", " + zipCode);
    }
}
