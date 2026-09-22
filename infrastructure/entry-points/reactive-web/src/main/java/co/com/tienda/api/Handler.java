package co.com.tienda.api;

import co.com.tienda.api.dto.ProductRequest;
import co.com.tienda.model.product.Product;
import co.com.tienda.usecase.product.ProductUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class Handler {
    private final ProductUseCase productUseCase;
    private final Validator validator;

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .doOnNext(this::validate)
                .map(this::toProduct)
                .flatMap(productUseCase::create)
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product));
    }

    public Mono<ServerResponse> findAllProducts(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productUseCase.findAll(), Product.class);
    }

    public Mono<ServerResponse> findProductById(ServerRequest request) {
        return productUseCase.findById(request.pathVariable("id"))
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ProductRequest.class)
                .doOnNext(this::validate)
                .map(this::toProduct)
                .flatMap(product -> productUseCase.update(id, product))
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        return productUseCase.delete(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    private void validate(ProductRequest request) {
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Product toProduct(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .price(request.price())
                .stock(request.stock())
                .build();
    }
}
