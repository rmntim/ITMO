package common.network.responses;

import common.utility.CommandName;

import java.util.List;

public class ShowResponse extends Response {
    public final List<Product> products;

    public ShowResponse(List<Product> products, String error) {
        super(CommandName.SHOW, error);
        this.products = products;
    }
}
