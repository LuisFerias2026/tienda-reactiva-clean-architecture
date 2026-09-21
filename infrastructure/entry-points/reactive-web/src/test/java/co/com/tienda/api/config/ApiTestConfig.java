package co.com.tienda.api.config;

import co.com.tienda.usecase.product.ProductUseCase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ApiTestConfig {
    @Bean
    ProductUseCase productUseCase() {
        return mock(ProductUseCase.class);
    }
}