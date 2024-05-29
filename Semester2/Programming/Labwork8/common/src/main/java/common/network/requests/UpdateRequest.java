package common.network.requests;

import common.domain.Dragon;
import common.user.User;
import common.utility.CommandName;

public class UpdateRequest extends Request {
    public final int id;
    public final Dragon updatedDragon;

    public UpdateRequest(int id, Dragon updatedDragon, User user) {
        super(CommandName.UPDATE, user);
        this.id = id;
        this.updatedDragon = updatedDragon;
    }
}
