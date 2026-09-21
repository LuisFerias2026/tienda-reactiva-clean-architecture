package co.com.tienda.r2dbc;

import co.com.tienda.model.product.Product;
import co.com.tienda.model.product.gateways.ProductRepository;
import co.com.tienda.r2dbc.entity.ProductData;
import org.springframework.data.domain.Example;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter implements ProductRepository {
    private final MyReactiveRepository repository;
    private final ObjectMapper mapper;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(toData(product)).map(this::toEntity);
    }

    @Override
    public Mono<Product> findById(String id) {
        return repository.findById(id).map(this::toEntity);
    }

    @Override
    public Flux<Product> findAll() {
        return repository.findAll().map(this::toEntity);
    }

    public Flux<Product> findByExample(Product product) {
        return repository.findAll(Example.of(toData(product))).map(this::toEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    private ProductData toData(Product product) {
        return mapper.map(product, ProductData.class);
    }

    private Product toEntity(ProductData data) {
        return mapper.map(data, Product.class);
    }
}
