package common.network.responses;

import common.user.User;
import common.utility.CommandName;

public class AuthenticateResponse extends Response {
    public final User user;

    public AuthenticateResponse(User user, String error) {
        super(CommandName.AUTHENTICATE, error);
        this.user = user;
    }
}
