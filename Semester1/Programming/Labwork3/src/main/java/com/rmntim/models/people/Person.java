package com.rmntim.models.people;

import com.rmntim.interfaces.Thinker;

import java.util.Objects;

public abstract class Person implements Thinker {
    protected final String name;
    protected final Sex sex;

    public Person(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public boolean isMale() {
        return sex == Sex.MALE;
    }

    @Override
    public String think(String thought) {
        return '"' + thought + '"' + ", --" + (isMale() ? " подумал " : " подумала ") + getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && sex == person.sex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sex);
    }

    @Override
    public String toString() {
        return name;
    }
}
