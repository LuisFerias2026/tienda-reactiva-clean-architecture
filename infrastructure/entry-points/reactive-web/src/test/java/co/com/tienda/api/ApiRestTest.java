package co.com.tienda.api;

import co.com.tienda.api.config.ApiTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {ApiRest.class})
@WebFluxTest
@Import(ApiTestConfig.class)
class ApiRestTest {

    @Autowired
    private WebTestClient webTestClient;

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
