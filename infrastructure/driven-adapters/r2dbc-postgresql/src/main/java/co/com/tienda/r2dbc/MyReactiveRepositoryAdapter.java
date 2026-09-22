package co.com.tienda.r2dbc;

import co.com.tienda.model.product.Product;
import co.com.tienda.model.product.gateways.ProductRepository;
import co.com.tienda.r2dbc.entity.ProductData;
import io.r2dbc.spi.R2dbcException;
import org.springframework.data.domain.Example;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter implements ProductRepository {
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    private final MyReactiveRepository repository;
    private final R2dbcEntityTemplate template;
    private final ObjectMapper mapper;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository,
                                       R2dbcEntityTemplate template,
                                       ObjectMapper mapper) {
        this.repository = repository;
        this.template = template;
        this.mapper = mapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductData data = toData(product);
        return repository.existsById(data.getId())
                .flatMap(exists -> exists
                        ? repository.save(data)
                        : template.insert(ProductData.class).using(data))
                .map(this::toEntity)
                .onErrorMap(this::isUniqueViolation,
                        error -> new IllegalStateException("Product name '" + product.getName() + "' already exists", error));
    }

    private boolean isUniqueViolation(Throwable throwable) {
        for (Throwable current = throwable; current != null; current = current.getCause()) {
            if (current instanceof R2dbcException r2dbcException
                    && UNIQUE_VIOLATION_SQL_STATE.equals(r2dbcException.getSqlState())) {
                return true;
            }
        }
        return false;
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
