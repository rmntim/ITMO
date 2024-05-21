package common.network.requests;

import common.domain.Product;
import common.user.User;
import common.utility.CommandName;

public class AddIfMaxRequest extends Request {
    public final Product product;

    public AddIfMaxRequest(Product product, User user) {
        super(CommandName.ADD_IF_MAX, user);
        this.product = product;
    }
}
