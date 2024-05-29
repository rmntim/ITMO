package common.user;

import java.io.Serializable;

public record User(int id, String name, String password) implements Comparable<User>, Serializable {
    public boolean validate() {
        return name().length() < 40;
    }

    public User copy(int id) {
        return new User(id, name(), password());
    }

    public User withoutPassword() {
        return new User(this.id(), this.name(), "");
    }

    @Override
    public String toString() {
        return name + ", id=" + id;
    }

    @Override
    public int compareTo(User user) {
        return this.id() - user.id();
    }
}
