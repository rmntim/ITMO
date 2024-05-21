package common.network.responses;

import common.utility.CommandName;

public class AddResponse extends Response {
    public final int newId;

    public AddResponse(int newId, String error) {
        super(CommandName.ADD, error);
        this.newId = newId;
    }
}
