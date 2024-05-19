package server.commands;

import common.network.requests.Request;
import common.network.responses.HeadResponse;
import common.network.responses.Response;
import server.repositories.ProductRepository;

public class Head extends Command {
    private final ProductRepository productRepository;

    public Head(ProductRepository productRepository) {
        super("head", "вывести первый элемент коллекции");
        this.productRepository = productRepository;
    }

    @Override
    public Response apply(Request request) {
        try {
            return new HeadResponse(productRepository.first(), null);
        } catch (Exception e) {
            return new HeadResponse(null, e.toString());
        }
    }
}
