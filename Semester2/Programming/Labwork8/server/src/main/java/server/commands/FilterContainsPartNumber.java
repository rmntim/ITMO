package server.commands;

import common.domain.Product;
import common.network.requests.FilterContainsPartNumberRequest;
import common.network.requests.Request;
import common.network.responses.FilterContainsPartNumberResponse;
import common.network.responses.Response;
import server.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class FilterContainsPartNumber extends Command {
    private final ProductRepository productRepository;

    public FilterContainsPartNumber(ProductRepository productRepository) {
        super("filter_contains_part_number <PN>", "вывести элементы, значение поля partNumber которых содержит заданную подстроку");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        var req = (FilterContainsPartNumberRequest) request;
        try {
            return new FilterContainsPartNumberResponse(filterByPartNumber(req.partNumber), null);
        } catch (Exception e) {
            return new FilterContainsPartNumberResponse(null, e.toString());
        }
    }

    private List<Product> filterByPartNumber(String partNumberSubstring) {
        return productRepository.get().stream()
                .filter(product -> (product.getPartNumber() != null && product.getPartNumber().contains(partNumberSubstring)))
                .sorted()
                .collect(Collectors.toList());
    }
}