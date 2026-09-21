package co.com.tienda.model.product.gateways;

import co.com.tienda.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
	Mono<Product> save(Product product);
	Mono<Product> findById(String id);
	Flux<Product> findAll();
	Mono<Void> deleteById(String id);
}
