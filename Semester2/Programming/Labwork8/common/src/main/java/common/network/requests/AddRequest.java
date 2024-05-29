package common.network.requests;

import common.domain.Dragon;
import common.user.User;
import common.utility.CommandName;

public class AddRequest extends Request {
    public final Dragon dragon;

    public AddRequest(Dragon dragon, User user) {
        super(CommandName.ADD, user);
        this.dragon = dragon;
    }
}
