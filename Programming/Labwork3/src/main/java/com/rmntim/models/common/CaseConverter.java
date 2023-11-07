package com.rmntim.models.common;

import com.rmntim.models.people.Group;

public class CaseConverter {
    private CaseConverter() {
    }

    public static String toDative(String name) {
        if (name.matches(".*(?i)[аеёоуиэя]")) {
            return name.substring(0, name.length() - 1) + "е";
        }
        return name + "у";
    }

    public static String toDative(Group<?> group) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < group.getMembers().size(); i++) {
            if (i == 0) {
                builder.append(CaseConverter.toDative(group.getMembers().get(i).getName()));
            } else if (i == group.getMembers().size() - 1) {
                builder.append(" и ").append(CaseConverter.toDative(group.getMembers().get(i).getName()));
            } else {
                builder.append(", ").append(CaseConverter.toDative(group.getMembers().get(i).getName()));
            }
        }
        return builder.toString();
    }
}
