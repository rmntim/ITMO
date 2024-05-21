package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class RegisterRequest extends Request {
    public RegisterRequest(User user) {
        super(CommandName.REGISTER, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}
