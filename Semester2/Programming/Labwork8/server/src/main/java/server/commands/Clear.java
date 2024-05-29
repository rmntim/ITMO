package server.commands;

import common.network.requests.Request;
import common.network.responses.ClearResponse;
import common.network.responses.Response;
import server.repositories.DragonRepository;

public class Clear extends Command {
    private final DragonRepository dragonRepository;

    public Clear(DragonRepository dragonRepository) {
        super("clear", "очистить коллекцию");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            dragonRepository.clear(request.getUser());
            return new ClearResponse(null);
        } catch (Exception e) {
            return new ClearResponse(e.toString());
        }
    }
}
