package common.network.responses;

import common.utility.CommandName;

public class HelpResponse extends Response {
    public final String helpMessage;

    public HelpResponse(String helpMessage, String error) {
        super(CommandName.HELP, error);
        this.helpMessage = helpMessage;
    }
}
