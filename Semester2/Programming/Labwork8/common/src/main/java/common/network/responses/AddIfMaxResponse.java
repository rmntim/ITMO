package common.network.responses;

import common.utility.CommandName;

public class AddIfMaxResponse extends Response {
    public final boolean isAdded;
    public final int newId;

    public AddIfMaxResponse(boolean isAdded, int newId, String error) {
        super(CommandName.ADD_IF_MAX, error);
        this.isAdded = isAdded;
        this.newId = newId;
    }
}
