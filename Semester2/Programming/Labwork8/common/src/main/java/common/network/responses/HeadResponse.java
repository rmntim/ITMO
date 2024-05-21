package common.network.responses;

import common.utility.CommandName;

public class HeadResponse extends Response {
    public final Product product;

    public HeadResponse(Product product, String error) {
        super(CommandName.HEAD, error);
        this.product = product;
    }
}
