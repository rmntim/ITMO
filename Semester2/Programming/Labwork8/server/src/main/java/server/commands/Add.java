package server.commands;

import common.network.requests.AddRequest;
import common.network.requests.Request;
import common.network.responses.AddResponse;
import common.network.responses.Response;
import server.repositories.DragonRepository;

public class Add extends Command {
    private final DragonRepository dragonRepository;

    public Add(DragonRepository dragonRepository) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (AddRequest) request;
        try {
            if (!req.dragon.validate()) {
                return new AddResponse(-1, "Поля продукта не валидны! Продукт не добавлен!");
            }
            var newId = dragonRepository.add(req.getUser(), req.dragon);
            return new AddResponse(newId, null);
        } catch (Exception e) {
            return new AddResponse(-1, e.toString());
        }
    }
}
