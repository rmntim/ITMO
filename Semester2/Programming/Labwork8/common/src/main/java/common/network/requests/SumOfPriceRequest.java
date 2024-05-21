package common.network.requests;

import common.user.User;
import common.utility.CommandName;

public class SumOfPriceRequest extends Request {
    public SumOfPriceRequest(User user) {
        super(CommandName.SUM_OF_PRICE, user);
    }
}
