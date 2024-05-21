package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class ClearRequest extends Request {
    public ClearRequest(User user) {
        super(CommandName.CLEAR, user);
    }
}
