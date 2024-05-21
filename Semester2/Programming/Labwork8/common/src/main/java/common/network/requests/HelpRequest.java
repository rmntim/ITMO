package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class HelpRequest extends Request {
    public HelpRequest(User user) {
        super(CommandName.HELP, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}
