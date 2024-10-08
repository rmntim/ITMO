package common.network.requests;

import common.domain.Dragon;
import common.user.User;
import common.utility.CommandName;

public class AddIfMaxRequest extends Request {
    public final Dragon dragon;

    public AddIfMaxRequest(Dragon dragon, User user) {
        super(CommandName.ADD_IF_MAX, user);
        this.dragon = dragon;
    }
}
