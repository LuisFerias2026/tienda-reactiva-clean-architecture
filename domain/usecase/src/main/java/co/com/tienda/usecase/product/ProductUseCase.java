package co.com.tienda.usecase.product;

import co.com.tienda.model.product.Product;
import co.com.tienda.model.product.gateways.ProductRepository;
import co.com.tienda.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {
	private final ProductRepository productRepository;
	private final EventsGateway eventsGateway;

	public Mono<Product> create(Product product) {
		return productRepository.save(product.toBuilder()
			.id(product.getId() == null ? java.util.UUID.randomUUID().toString() : product.getId())
			.build())
			.flatMap(saved -> eventsGateway.emit(saved).thenReturn(saved));
	}

	public Mono<Product> findById(String id) {
		return productRepository.findById(id);
	}

	public Flux<Product> findAll() {
		return productRepository.findAll();
	}

	public Mono<Product> update(String id, Product product) {
		return productRepository.findById(id)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found: " + id)))
				.map(existing -> product.toBuilder().id(existing.getId()).build())
				.flatMap(productRepository::save);
	}

	public Mono<Void> delete(String id) {
		return productRepository.deleteById(id);
	}
}
