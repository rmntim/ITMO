package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class HeadRequest extends Request {
    public HeadRequest(User user) {
        super(CommandName.HEAD, user);
    }
}
