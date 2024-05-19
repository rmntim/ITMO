package server.commands;

import common.exceptions.BadOwnerException;
import common.network.requests.Request;
import common.network.requests.UpdateRequest;
import common.network.responses.Response;
import common.network.responses.UpdateResponse;
import server.repositories.ProductRepository;

public class Update extends Command {
    private final ProductRepository productRepository;

    public Update(ProductRepository productRepository) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (UpdateRequest) request;
        try {
            if (!productRepository.checkExist(req.id)) {
                return new UpdateResponse("Продукта с таким ID в коллекции нет!");
            }
            if (!req.updatedProduct.validate()) {
                return new UpdateResponse("Поля продукта не валидны! Продукт не обновлен!");
            }

            productRepository.update(req.getUser(), req.updatedProduct);
            return new UpdateResponse(null);
        } catch (BadOwnerException e) {
            return new UpdateResponse("BAD_OWNER");
        } catch (Exception e) {
            return new UpdateResponse(e.toString());
        }
    }
}
