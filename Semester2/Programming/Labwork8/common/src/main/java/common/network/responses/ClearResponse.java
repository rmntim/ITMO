package common.network.responses;

import common.utility.CommandName;

public class ClearResponse extends Response {
    public ClearResponse(String error) {
        super(CommandName.CLEAR, error);
    }
}
