package com.rmntim.models.common;

import java.util.ArrayList;
import java.util.List;

import com.rmntim.exceptions.NotEnoughSpaceException;
import com.rmntim.interfaces.Plurable;

public abstract class Wearable implements Plurable, Cloneable {
    private final String name;
    private ArrayList<Item> items;
    private int currentWeight;
    private final int weightCapacity;

    protected Wearable(String name, int capacity) {
        this.name = name;
        this.items = new ArrayList<>();
        this.currentWeight = 0;
        this.weightCapacity = capacity;
    }

    public int getWeightCapacity() {
        return weightCapacity;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void addItem(Item item) throws NotEnoughSpaceException {
        if (item.weight() + getCurrentWeight() > weightCapacity)
            throw new NotEnoughSpaceException(item);
        items.add(item);
        setCurrentWeight(getCurrentWeight() + item.weight());
    }

    public void addItems(List<Item> items) throws NotEnoughSpaceException {
        for (Item item : items)
            addItem(item);
    }

    @Override
    public String toString() {
        return "Wearable [name=" + name + "]";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Wearable w = (Wearable) super.clone();

        w.items = new ArrayList<>();

        for (Item item : items) {
            w.items.add(item);
        }

        return w;
    }
}
