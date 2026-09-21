package co.com.tienda.api;
import co.com.tienda.model.product.Product;
import co.com.tienda.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiRest {
    private final ProductUseCase productUseCase;

    @PostMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Product> create(@RequestBody Product product) {
        return productUseCase.create(product);
    }

    @GetMapping(path = "/products")
    public Flux<Product> findAll() {
        return productUseCase.findAll();
    }

    @GetMapping(path = "/products/{id}")
    public Mono<Product> findById(@PathVariable String id) {
        return productUseCase.findById(id);
    }

    @PutMapping(path = "/products/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Product> update(@PathVariable String id, @RequestBody Product product) {
        return productUseCase.update(id, product);
    }

    @DeleteMapping(path = "/products/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return productUseCase.delete(id);
    }
}
