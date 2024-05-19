package server.commands;

import common.domain.Product;
import common.network.requests.AddIfMinRequest;
import common.network.requests.Request;
import common.network.responses.AddIfMinResponse;
import common.network.responses.Response;
import server.repositories.ProductRepository;

public class AddIfMin extends Command {
    private final ProductRepository productRepository;

    public AddIfMin(ProductRepository productRepository) {
        super("add_if_min {element}", "добавить новый элемент в коллекцию, если его цена меньше минимальной цены этой коллекции");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            var req = (AddIfMinRequest) request;
            var minPrice = minPrice();
            if (req.product.getPrice() < minPrice) {
                var newId = productRepository.add(req.getUser(), req.product);
                return new AddIfMinResponse(true, newId, null);
            }
            return new AddIfMinResponse(false, -1, null);
        } catch (Exception e) {
            return new AddIfMinResponse(false, -1, e.toString());
        }
    }

    private Long minPrice() {
        return productRepository.get().stream()
                .map(Product::getPrice)
                .mapToLong(Long::longValue)
                .min()
                .orElse(Long.MAX_VALUE);
    }
}
