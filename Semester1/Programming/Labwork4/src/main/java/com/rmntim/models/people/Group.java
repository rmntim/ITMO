package com.rmntim.models.people;

import com.rmntim.exceptions.NoSuchMemberException;
import com.rmntim.exceptions.NotEnoughSpaceException;
import com.rmntim.models.common.Action;
import com.rmntim.models.common.Wearable;
import com.rmntim.models.common.Action.Attach;
import com.rmntim.models.common.Action.Take;

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

    public String wear(Wearable wearable) {
        var builder = new StringBuilder();

        for (int i = 0; i < members.size() - 1; ++i) {
            if (i != 0)
                builder.append(", ");

            T member = members.get(i);
            try {
                member.setCurrentWearable((Wearable) wearable.clone());
            } catch (CloneNotSupportedException cnse) {
                cnse.printStackTrace();
                continue;
            }

            builder.append(member);
        }
        builder.append(" и ").append(members.get(members.size() - 1));

        builder.append(" надели ").append(wearable.toPlural());

        return builder.toString();
    }

    public String performActions(List<Action> actions) throws NotEnoughSpaceException {
        var builder = new StringBuilder();

        builder.append("Каждый");
        for (int i = 0; i < actions.size(); ++i) {
            Action action = actions.get(i);
            if (i != actions.size() - 1)
                builder.append(",");

            if (action instanceof Take) {
                for (T member : members) {
                    Wearable currentWearable = member.getCurrentWearable();
                    if (currentWearable == null)
                        continue;
                    currentWearable.addItems(((Take) action).getItems());
                }
            } else if (action instanceof Attach) {
                for (T member : members) {
                    Wearable currentWearable = member.getCurrentWearable();
                    if (currentWearable == null)
                        continue;
                    currentWearable.addItems(((Attach) action).getItems());
                }
            }

            builder.append(" ").append(action.asSingular());
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Group<?> group = (Group<?>) obj;
        return Objects.equals(members, group.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members);
    }
}
