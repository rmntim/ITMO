package common.network.responses;

import common.utility.CommandName;

import java.time.LocalDateTime;

public class InfoResponse extends Response {
    public final String type;
    public final String size;
    public final LocalDateTime lastSaveTime;
    public final LocalDateTime lastInitTime;

    public InfoResponse(String type, String size, LocalDateTime lastSaveTime, LocalDateTime lastInitTime, String error) {
        super(CommandName.INFO, error);
        this.type = type;
        this.size = size;
        this.lastSaveTime = lastSaveTime;
        this.lastInitTime = lastInitTime;
    }
}
