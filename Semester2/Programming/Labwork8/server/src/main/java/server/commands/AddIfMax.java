package server.commands;

import common.domain.Product;
import common.network.requests.*;
import common.network.responses.*;
import server.repositories.ProductRepository;

public class AddIfMax extends Command {
  private final ProductRepository productRepository;

  public AddIfMax(ProductRepository productRepository) {
    super("add_if_max {element}", "добавить новый элемент в коллекцию, если его цена превышает максимальную цену этой коллекции");
    this.productRepository = productRepository;
  }

  @Override
  public Response apply(Request request) {
    try {
      var req = (AddIfMaxRequest) request;
      var maxPrice = maxPrice();
      if (req.product.getPrice() > maxPrice) {
        var newId = productRepository.add(req.getUser(), req.product);
        return new AddIfMaxResponse(true, newId, null);
      }
      return new AddIfMaxResponse(false, -1, null);
    } catch (Exception e) {
      return new AddIfMaxResponse(false, -1, e.toString());
    }
  }

  private Long maxPrice() {
    return productRepository.get().stream()
      .map(Product::getPrice)
      .mapToLong(Long::longValue)
      .max()
      .orElse(-1);
  }
}
