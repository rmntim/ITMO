package common.network.responses;

import common.utility.CommandName;

public class RemoveByIdResponse extends Response {
    public RemoveByIdResponse(String error) {
        super(CommandName.REMOVE_BY_ID, error);
    }
}
