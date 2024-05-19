package server.commands;

import common.network.requests.Request;
import common.network.responses.*;
import server.repositories.ProductRepository;

public class Show extends Command {
  private final ProductRepository productRepository;

  public Show(ProductRepository productRepository) {
    super("show", "вывести все элементы коллекции");
    this.productRepository = productRepository;
  }

  @Override
  public Response apply(Request request) {
    try {
      return new ShowResponse(productRepository.sorted(), null);
    } catch (Exception e) {
      return new ShowResponse(null, e.toString());
    }
  }
}
