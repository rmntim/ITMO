package common.network.responses;

import common.utility.CommandName;

public class SumOfPriceResponse extends Response {
    public final long sum;

    public SumOfPriceResponse(long sum, String error) {
        super(CommandName.SUM_OF_PRICE, error);
        this.sum = sum;
    }
}
