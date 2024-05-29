package server.commands;

import common.exceptions.BadOwnerException;
import common.network.requests.RemoveByIdRequest;
import common.network.requests.Request;
import common.network.responses.RemoveByIdResponse;
import common.network.responses.Response;
import server.repositories.DragonRepository;

public class RemoveById extends Command {
    private final DragonRepository dragonRepository;

    public RemoveById(DragonRepository dragonRepository) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (RemoveByIdRequest) request;

        try {
            if (!dragonRepository.checkExist(req.id)) {
                return new RemoveByIdResponse("Продукта с таким ID в коллекции нет!");
            }

            var removedCount = dragonRepository.remove(req.getUser(), req.id);
            if (removedCount <= 0) {
                return new RemoveByIdResponse("Ничего не удалено!");
            }
            return new RemoveByIdResponse(null);
        } catch (BadOwnerException e) {
            return new RemoveByIdResponse("Зафиксирована попытка удалить чужой продукт!");
        } catch (Exception e) {
            return new RemoveByIdResponse(e.toString());
        }
    }
}
