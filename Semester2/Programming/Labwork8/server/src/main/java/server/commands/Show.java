package server.commands;

import common.network.requests.Request;
import common.network.responses.Response;
import common.network.responses.ShowResponse;
import server.repositories.DragonRepository;

public class Show extends Command {
    private final DragonRepository dragonRepository;

    public Show(DragonRepository dragonRepository) {
        super("show", "вывести все элементы коллекции");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            return new ShowResponse(dragonRepository.sorted(), null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
    }
}
