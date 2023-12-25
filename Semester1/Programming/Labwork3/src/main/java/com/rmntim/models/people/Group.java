package com.rmntim.models.people;

import java.util.List;
import java.util.Objects;

public class Group<T extends Person> {
    private List<T> members;

    public Group(List<T> members) {
        setMembers(members);
    }

    public List<T> getMembers() {
        return members;
    }

    public void setMembers(List<T> members) {
        this.members = members;
    }

    public void addMember(T member) {
        this.members.add(member);
    }

    public boolean contains(T member) {
        return members.contains(member);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Group<?> group = (Group<?>) obj;
        return Objects.equals(members, ((Group<?>) obj).members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members);
    }
}
