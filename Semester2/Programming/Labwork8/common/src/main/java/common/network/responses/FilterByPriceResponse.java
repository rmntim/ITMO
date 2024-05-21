package common.network.responses;

import common.utility.CommandName;

import java.util.List;

public class FilterByPriceResponse extends Response {
    public final List<Product> filteredProducts;

    public FilterByPriceResponse(List<Product> filteredProducts, String error) {
        super(CommandName.FILTER_BY_PRICE, error);
        this.filteredProducts = filteredProducts;
    }
}
