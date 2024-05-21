package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class FilterByPriceRequest extends Request {
    public final long price;

    public FilterByPriceRequest(long price, User user) {
        super(CommandName.FILTER_BY_PRICE, user);
        this.price = price;
    }
}
