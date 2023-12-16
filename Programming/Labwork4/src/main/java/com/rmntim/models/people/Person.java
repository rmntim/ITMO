package com.rmntim.models.people;

import com.rmntim.exceptions.NotEnoughSpaceException;
import com.rmntim.interfaces.Thinker;
import com.rmntim.models.common.Action;
import com.rmntim.models.common.Wearable;
import com.rmntim.models.common.Action.Hang;

import java.util.Objects;

public abstract class Person implements Thinker {
    private final String name;
    private final Sex sex;
    private Wearable currentWearable;

    public Wearable getCurrentWearable() {
        return currentWearable;
    }

    public void setCurrentWearable(Wearable currentWearable) {
        this.currentWearable = currentWearable;
    }

    public Person(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public Sex getSex() {
        return sex;
    }

    public boolean isMale() {
        return sex == Sex.MALE;
    }

    public String performAction(Action action) throws NotEnoughSpaceException {
        if (action instanceof Hang) {
            getCurrentWearable().addItems(((Hang) action).getItems());
        }

        return name + " " + action.asSingular();
    }

    @Override
    public String think(String thought) {
        return '"' + thought + '"' + ", --" + (isMale() ? " подумал " : " подумала ") + getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
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
