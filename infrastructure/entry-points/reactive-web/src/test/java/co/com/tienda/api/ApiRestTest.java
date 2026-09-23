package co.com.tienda.api;

import co.com.tienda.api.config.ApiTestConfig;
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

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
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
                .expectBody().json("[]");
    }

}
