package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class InfoRequest extends Request {
    public InfoRequest(User user) {
        super(CommandName.INFO, user);
    }
}
