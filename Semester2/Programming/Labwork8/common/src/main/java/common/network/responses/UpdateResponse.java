package common.network.responses;

import common.utility.CommandName;

public class UpdateResponse extends Response {
    public UpdateResponse(String error) {
        super(CommandName.UPDATE, error);
    }
}
