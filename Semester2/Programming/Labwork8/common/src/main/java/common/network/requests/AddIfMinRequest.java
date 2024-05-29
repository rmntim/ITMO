package common.network.requests;

import common.domain.Dragon;
import common.user.User;
import common.utility.CommandName;

public class AddIfMinRequest extends Request {
    public final Dragon dragon;

    public AddIfMinRequest(Dragon dragon, User user) {
        super(CommandName.ADD_IF_MIN, user);
        this.dragon = dragon;
    }
}
