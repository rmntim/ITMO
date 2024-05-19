package server.commands;

import common.network.requests.AddRequest;
import common.network.requests.Request;
import common.network.responses.AddResponse;
import common.network.responses.Response;
import server.repositories.ProductRepository;

public class Add extends Command {
    private final ProductRepository productRepository;

    public Add(ProductRepository productRepository) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (AddRequest) request;
        try {
            if (!req.product.validate()) {
                return new AddResponse(-1, "Поля продукта не валидны! Продукт не добавлен!");
            }
            var newId = productRepository.add(req.getUser(), req.product);
            return new AddResponse(newId, null);
        } catch (Exception e) {
            return new AddResponse(-1, e.toString());
        }
    }
}
