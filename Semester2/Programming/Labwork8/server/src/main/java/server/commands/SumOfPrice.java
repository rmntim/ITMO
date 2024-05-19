package server.commands;

import common.domain.Product;
import common.network.requests.Request;
import common.network.responses.*;
import server.repositories.ProductRepository;

public class SumOfPrice extends Command {
    private final ProductRepository productRepository;

    public SumOfPrice(ProductRepository productRepository) {
        super("sum_of_price", "вывести сумму значений поля price для всех элементов коллекции");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            return new SumOfPriceResponse(getSumOfPrice(), null);
        } catch (Exception e) {
            return new SumOfPriceResponse(-1, e.toString());
        }
    }

    private Long getSumOfPrice() {
        return productRepository.get().stream()
                .map(Product::getPrice)
                .mapToLong(Long::longValue)
                .sum();
    }
}
