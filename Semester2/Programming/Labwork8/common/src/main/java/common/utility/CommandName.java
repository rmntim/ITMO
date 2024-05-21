package common.utility;

public enum CommandName {
    AUTHENTICATE("authenticate"),
    REGISTER("register"),
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    HEAD("head"),
    ADD_IF_MAX("add_if_max"),
    ADD_IF_MIN("add_if_min"),
    SUM_OF_PRICE("sum_of_price"),
    FILTER_BY_PRICE("filter_by_price"),
    FILTER_CONTAINS_PART_NUMBER("filter_contains_part_number");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
