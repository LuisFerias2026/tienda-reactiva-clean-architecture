package co.com.tienda.api;

import co.com.tienda.api.dto.ApiResponse;
import co.com.tienda.api.dto.ProductRequest;
import co.com.tienda.api.mapper.ProductMapper;
import co.com.tienda.api.validator.RequestValidator;
import co.com.tienda.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final ProductUseCase productUseCase;
    private final RequestValidator requestValidator;
    private final ProductMapper productMapper;

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(requestValidator::validate)
                .map(productMapper::toProduct)
                .flatMap(productUseCase::create)
                .flatMap(product -> respond(HttpStatus.CREATED, "Product created", product));
    }

    public Mono<ServerResponse> findAllProducts(ServerRequest request) {
        return productUseCase.findAll()
                .collectList()
                .flatMap(products -> respond(HttpStatus.OK, "Products retrieved", products));
    }

    public Mono<ServerResponse> findProductById(ServerRequest request) {
        return productUseCase.findById(request.pathVariable("id"))
                .flatMap(product -> respond(HttpStatus.OK, "Product retrieved", product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ProductRequest.class)
                .flatMap(requestValidator::validate)
                .map(productMapper::toProduct)
                .flatMap(product -> productUseCase.update(id, product))
                .flatMap(product -> respond(HttpStatus.OK, "Product updated", product));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        return productUseCase.delete(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    private <T> Mono<ServerResponse> respond(HttpStatus status, String message, T data) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ApiResponse.of(status, message, data));
    }
}
