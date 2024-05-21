package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class AuthenticateRequest extends Request {
    public AuthenticateRequest(User user) {
        super(CommandName.AUTHENTICATE, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}
