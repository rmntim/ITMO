package common.network.responses;

import common.utility.CommandName;

import java.util.List;

public class FilterContainsPartNumberResponse extends Response {
    public final List<Product> filteredProducts;

    public FilterContainsPartNumberResponse(List<Product> filteredProducts, String error) {
        super(CommandName.FILTER_CONTAINS_PART_NUMBER, error);
        this.filteredProducts = filteredProducts;
    }
}
