package server.commands;

import common.domain.Dragon;
import common.network.requests.AddIfMaxRequest;
import common.network.requests.Request;
import common.network.responses.AddIfMaxResponse;
import common.network.responses.Response;
import server.repositories.DragonRepository;

public class AddIfMax extends Command {
    private final DragonRepository dragonRepository;

    public AddIfMax(DragonRepository dragonRepository) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его возраст превышает максимальную цену этой коллекции");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            var req = (AddIfMaxRequest) request;
            var maxAge = maxAge();
            if (req.dragon.age() > maxAge) {
                var newId = dragonRepository.add(req.getUser(), req.dragon);
                return new AddIfMaxResponse(true, newId, null);
            }
            return new AddIfMaxResponse(false, -1, null);
        } catch (Exception e) {
            return new AddIfMaxResponse(false, -1, e.toString());
        }
    }

    private Long maxAge() {
        return dragonRepository.get().stream()
                .map(Dragon::age)
                .reduce(Long::max)
                .orElse(-1L);
    }
}
