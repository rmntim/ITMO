package server.commands;

import common.network.requests.Request;
import common.network.responses.InfoResponse;
import common.network.responses.Response;
import server.repositories.DragonRepository;

public class Info extends Command {
    private final DragonRepository dragonRepository;

    public Info(DragonRepository dragonRepository) {
        super("info", "вывести информацию о коллекции");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        var lastInitTime = dragonRepository.getLastInitTime();
        var lastSaveTime = dragonRepository.getLastSaveTime();
        return new InfoResponse(dragonRepository.type(), String.valueOf(dragonRepository.size()), lastSaveTime, lastInitTime, null);
    }
}
