package common.network.requests;

import common.domain.Product;
import common.user.User;
import common.utility.CommandName;

public class UpdateRequest extends Request {
    public final int id;
    public final Product updatedProduct;

    public UpdateRequest(int id, Product updatedProduct, User user) {
        super(CommandName.UPDATE, user);
        this.id = id;
        this.updatedProduct = updatedProduct;
    }
}
