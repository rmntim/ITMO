package common.network.responses;

import common.domain.Dragon;
import common.utility.CommandName;

import java.util.List;

public class ShowResponse extends Response {
    public final List<Dragon> dragons;

    public ShowResponse(List<Dragon> dragons, String error) {
        super(CommandName.SHOW, error);
        this.dragons = dragons;
    }
}
