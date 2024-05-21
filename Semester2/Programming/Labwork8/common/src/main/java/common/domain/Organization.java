package common.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Organization implements Comparable<Organization>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; // Поле не может быть null, Строка не может быть пустой
    private final long employeesCount; // Значение поля должно быть больше 0
    private final OrganizationType type; // Поле не может быть null
    private final Address postalAddress; // Поле не может быть null

    public Organization(Integer id, String name, long employeesCount, OrganizationType type, Address postalAddress) {
        this.id = id;
        this.name = name;
        this.employeesCount = employeesCount;
        this.type = type;
        this.postalAddress = postalAddress;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (employeesCount <= 0) return false;
        if (type == null) return false;
        return postalAddress == null || postalAddress.validate();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    @Override
    public int compareTo(Organization organization) {
        return (this.id - organization.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return employeesCount == that.employeesCount && Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type && Objects.equals(postalAddress, that.postalAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, employeesCount, type, postalAddress);
    }

    @Override
    public String toString() {
        return "Организация \"" + name + "\" №" + id +
                "; Число сотрудников: " + employeesCount +
                "; Вид: " + type +
                "; Адрес: " + postalAddress;
    }
}
