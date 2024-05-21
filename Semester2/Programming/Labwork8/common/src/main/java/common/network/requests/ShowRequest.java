package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class ShowRequest extends Request {
    public ShowRequest(User user) {
        super(CommandName.SHOW, user);
    }
}
