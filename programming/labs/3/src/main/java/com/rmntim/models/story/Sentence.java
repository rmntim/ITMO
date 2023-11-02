package com.rmntim.models.story;

import java.util.Objects;

public class Sentence {
    private final String text;

    public Sentence(String text) {
        this.text = text;
    }

    public void print() {
        System.out.println(text + ".");
    }

    public Sentence then(String text) {
        return new Sentence(this.text + ", " + text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return Objects.equals(text, sentence.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return text + ".";
    }
}
