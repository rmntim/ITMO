package common.network.responses;

import common.user.User;
import common.utility.CommandName;

public class RegisterResponse extends Response {
    public final User user;

    public RegisterResponse(User user, String error) {
        super(CommandName.REGISTER, error);
        this.user = user;
    }
}
