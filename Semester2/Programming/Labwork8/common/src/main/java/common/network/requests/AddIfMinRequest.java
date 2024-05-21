package common.network.requests;

import common.domain.Product;
import common.user.User;
import common.utility.CommandName;

public class AddIfMinRequest extends Request {
    public final Product product;

    public AddIfMinRequest(Product product, User user) {
        super(CommandName.ADD_IF_MIN, user);
        this.product = product;
    }
}
