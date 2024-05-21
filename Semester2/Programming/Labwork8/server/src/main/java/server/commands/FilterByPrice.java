package server.commands;

import common.domain.Product;
import common.network.requests.FilterByPriceRequest;
import common.network.requests.Request;
import common.network.responses.FilterByPriceResponse;
import common.network.responses.Response;
import server.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByPrice extends Command {
    private final ProductRepository productRepository;

    public FilterByPrice(ProductRepository productRepository) {
        super("filter_by_price <PRICE>", "вывести элементы, значение поля price которых равно заданному");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (FilterByPriceRequest) request;
        try {
            return new FilterByPriceResponse(filterByPrice(req.price), null);
        } catch (Exception e) {
            return new FilterByPriceResponse(null, e.toString());
        }
    }

    private List<Product> filterByPrice(Long price) {
        return productRepository.get().stream()
                .filter(product -> (product.getPrice().equals(price)))
                .sorted()
                .collect(Collectors.toList());
    }
}
