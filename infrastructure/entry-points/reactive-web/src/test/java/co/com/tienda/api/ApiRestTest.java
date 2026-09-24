package co.com.tienda.api;

import co.com.tienda.api.config.ApiTestConfig;
import co.com.tienda.api.mapper.ProductMapper;
import co.com.tienda.api.validator.RequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.BeforeEach;
import reactor.core.publisher.Flux;
import co.com.tienda.usecase.product.ProductUseCase;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, RequestValidator.class, ProductMapper.class})
@WebFluxTest
@Import(ApiTestConfig.class)
class ApiRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductUseCase productUseCase;

    @BeforeEach
    void setupMocks() {
        when(productUseCase.findAll()).thenReturn(Flux.empty());
    }

    @Test
    void findProducts() {
        webTestClient.get()
                .uri("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("Products retrieved")
                .jsonPath("$.data").isArray()
                .jsonPath("$.data").isEmpty();
    }

}
