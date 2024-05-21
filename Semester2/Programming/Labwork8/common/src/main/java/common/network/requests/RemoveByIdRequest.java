package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class RemoveByIdRequest extends Request {
    public final int id;

    public RemoveByIdRequest(int id, User user) {
        super(CommandName.REMOVE_BY_ID, user);
        this.id = id;
    }
}
