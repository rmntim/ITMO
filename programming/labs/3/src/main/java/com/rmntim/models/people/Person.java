package com.rmntim.models.people;

import com.rmntim.interfaces.Captain;
import com.rmntim.interfaces.HasCase;
import com.rmntim.interfaces.Thinker;
import com.rmntim.models.common.Order;

import java.util.Objects;
import java.util.StringJoiner;

public class Person implements Thinker, Captain, HasCase {
    private String name;
    private Sex sex;

    public Person(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return sex == Sex.MALE;
    }

    @Override
    public String think(String thought) {
        return '"' + thought + '"' + ", --" + (isMale() ? " подумал " : " подумала ") + getName();
    }

    @Override
    public String giveOrder(Group<?> group, Order order) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(sex.getPronoun());
        joiner.add(order.getMoment());
        joiner.add(isMale() ? "отдал" : "отдала");
        if (order.hasCommunicator())
            joiner.add("по " + order.getCommunicator().dativeCase());
        joiner.add("приказ");
        joiner.add(group.dativeCase());
        joiner.add(order.getActions());
        return joiner.toString();
    }

    @Override
    public String giveOrder(Person person, Order order) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(sex.getPronoun());
        joiner.add(order.getMoment());
        joiner.add(isMale() ? "отдал" : "отдала");
        if (order.hasCommunicator())
            joiner.add("по " + order.getCommunicator().dativeCase());
        joiner.add("приказ");
        joiner.add(person.dativeCase());
        joiner.add(order.getActions());
        return joiner + ".";
    }

    public String dativeCase() {
        if (getName().matches(".*(?i)[аеёоуиэя]")) {
            return getName().substring(0, getName().length() - 1) + "е";
        }

        return getName() + "у";
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
