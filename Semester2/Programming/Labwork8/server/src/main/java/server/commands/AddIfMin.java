package server.commands;

import common.domain.Dragon;
import common.network.requests.AddIfMinRequest;
import common.network.requests.Request;
import common.network.responses.AddIfMinResponse;
import common.network.responses.Response;
import server.repositories.DragonRepository;

public class AddIfMin extends Command {
    private final DragonRepository dragonRepository;

    public AddIfMin(DragonRepository dragonRepository) {
        super("add_if_min {element}", "добавить новый элемент в коллекцию, если его возраст меньше минимальной цены этой коллекции");
        this.dragonRepository = dragonRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            var req = (AddIfMinRequest) request;
            var minAge = minAge();
            if (req.dragon.age() < minAge) {
                var newId = dragonRepository.add(req.getUser(), req.dragon);
                return new AddIfMinResponse(true, newId, null);
            }
            return new AddIfMinResponse(false, -1, null);
        } catch (Exception e) {
            return new AddIfMinResponse(false, -1, e.toString());
        }
    }

    private Long minAge() {
        return dragonRepository.get().stream()
                .map(Dragon::age)
                .reduce(Long::min)
                .orElse(Long.MAX_VALUE);
    }
}
