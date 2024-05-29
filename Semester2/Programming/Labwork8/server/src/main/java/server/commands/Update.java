package server.commands;

import common.exceptions.BadOwnerException;
import common.network.requests.Request;
import common.network.requests.UpdateRequest;
import common.network.responses.Response;
import common.network.responses.UpdateResponse;
import server.repositories.DragonRepository;

public class Update extends Command {
    private final DragonRepository dragonRepository;

    public Update(DragonRepository dragonRepository) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (UpdateRequest) request;
        try {
            if (!dragonRepository.checkExist(req.id)) {
                return new UpdateResponse("Продукта с таким ID в коллекции нет!");
            }
            if (!req.updatedDragon.validate()) {
                return new UpdateResponse("Поля продукта не валидны! Продукт не обновлен!");
            }

            dragonRepository.update(req.getUser(), req.updatedDragon);
            return new UpdateResponse(null);
        } catch (BadOwnerException e) {
            return new UpdateResponse("BAD_OWNER");
        } catch (Exception e) {
            return new UpdateResponse(e.toString());
        }
    }
}
