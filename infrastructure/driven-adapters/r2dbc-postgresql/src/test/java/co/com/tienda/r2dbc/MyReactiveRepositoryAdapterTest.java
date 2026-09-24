package co.com.tienda.r2dbc;

import co.com.tienda.model.product.Product;
import co.com.tienda.r2dbc.entity.ProductData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveInsertOperation;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    R2dbcEntityTemplate template;

    @Test
    void mustFindValueById() {
        ProductData data = ProductData.builder().id("1").name("Cafe").build();
        when(repository.findById("1")).thenReturn(Mono.just(data));

        Mono<Product> result = repositoryAdapter.findById("1");

        StepVerifier.create(result)
            .expectNextMatches(value -> value.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        ProductData data = ProductData.builder().id("1").name("Cafe").build();
        when(repository.findAll()).thenReturn(Flux.just(data));

        Flux<Product> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
            .expectNextMatches(value -> value.getName().equals("Cafe"))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void mustFindByExample() {
        ProductData data = ProductData.builder().id("1").name("Cafe").build();
        Product product = Product.builder().name("Cafe").build();
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(data));

        Flux<Product> result = repositoryAdapter.findByExample(product);

        StepVerifier.create(result)
            .expectNextMatches(value -> value.getName().equals("Cafe"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        Product product = Product.builder().id("1").name("Cafe").build();
        when(repository.existsById("1")).thenReturn(Mono.just(true));
        when(repository.save(any(ProductData.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Product> result = repositoryAdapter.save(product);

        StepVerifier.create(result)
            .expectNextMatches(value -> value.getName().equals("Cafe"))
                .verifyComplete();
    }

    @Test
    void mustInsertValueWhenNotExists() {
        Product product = Product.builder().id("2").name("Te").build();
        when(repository.existsById("2")).thenReturn(Mono.just(false));
        ReactiveInsertOperation.ReactiveInsert<ProductData> insertOp = new ReactiveInsertOperation.ReactiveInsert<>() {
            @Override
            public ReactiveInsertOperation.TerminatingInsert<ProductData> into(String table) {
                return this;
            }

            @Override
            public ReactiveInsertOperation.TerminatingInsert<ProductData> into(SqlIdentifier table) {
                return this;
            }

            @Override
            public Mono<ProductData> using(ProductData value) {
                return Mono.just(value);
            }
        };
        when(template.insert(ProductData.class)).thenReturn(insertOp);

        Mono<Product> result = repositoryAdapter.save(product);

        StepVerifier.create(result)
            .expectNextMatches(value -> value.getName().equals("Te"))
                .verifyComplete();
    }
}
