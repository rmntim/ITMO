package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class AddRequest extends Request {
    public final Product product;

    public AddRequest(Product product, User user) {
        super(CommandName.ADD, user);
        this.product = product;
    }
}
