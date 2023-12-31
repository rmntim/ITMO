package com.rmntim.models.story;

import com.rmntim.interfaces.Tellable;
import com.rmntim.models.people.Group;
import com.rmntim.models.people.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

public class Story implements Tellable {
    private final ArrayList<Sentence> sentences;
    private final HashMap<String, Person> characters;
    private final ArrayList<Group<Person>> groups;

    public Story() {
        this.sentences = new ArrayList<>();
        this.characters = new HashMap<>();
        this.groups = new ArrayList<>();
    }

    public void addCharacter(Person character) {
        this.characters.put(character.getName(), character);
    }

    public Person getCharacter(String name) {
        return this.characters.get(name);
    }

    public void addCharacterGroup(Group<Person> group) {
        this.groups.add(group);
        for (Person person : group.getMembers()) {
            this.characters.put(person.getName(), person);
        }
    }

    public Group<Person> getCharacterGroup(int index) {
        return this.groups.get(index);
    }

    public void tell() {
        for (Sentence sentence : sentences) {
            sentence.print();
        }
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story = (Story) o;
        return Objects.equals(sentences, story.sentences) &&
                Objects.equals(characters, story.characters) &&
                Objects.equals(groups, story.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentences, characters, groups);
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner("\n");
        for (Sentence sentence : sentences) {
            joiner.add(sentence.toString());
        }
        return joiner.toString();
    }
}
