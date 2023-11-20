package com.rmntim.models.people;

import com.rmntim.exceptions.NoSuchMemberException;
import com.rmntim.interfaces.Wearable;
import com.rmntim.models.common.Action;

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

    public T getMemberByName(String name) throws NoSuchMemberException {
        for (var member : members) {
            if (member.getName().equals(name))
                return member;
        }

        throw new NoSuchMemberException(name);
    }

    public void setMembers(List<T> members) {
        this.members = members;
    }

    public String wear(Wearable item) {
        var builder = new StringBuilder();

        for (int i = 0; i < members.size() - 1; ++i) {
            if (i != 0)
                builder.append(", ");
            builder.append(members.get(i));
        }
        builder.append(" и ").append(members.get(members.size() - 1));

        builder.append(" надели ").append(item.plural());

        return builder.toString();
    }

    public String performActions(List<Action> actions) {
        var builder = new StringBuilder();

        builder.append("Каждый");
        for (int i = 0; i < actions.size(); ++i) {
            builder.append(" ").append(actions.get(i).asSingular());
            if (i != actions.size() - 1)
                builder.append(",");
        }

        return builder.toString();
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
