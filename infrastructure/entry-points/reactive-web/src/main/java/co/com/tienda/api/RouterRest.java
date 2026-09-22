package co.com.tienda.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(Handler handler) {
        return RouterFunctions.route()
                .POST("/api/products", contentType(MediaType.APPLICATION_JSON), handler::createProduct)
                .GET("/api/products", accept(MediaType.APPLICATION_JSON), handler::findAllProducts)
                .GET("/api/products/{id}", accept(MediaType.APPLICATION_JSON), handler::findProductById)
                .PUT("/api/products/{id}", contentType(MediaType.APPLICATION_JSON), handler::updateProduct)
                .DELETE("/api/products/{id}", handler::deleteProduct)
                .build();
    }
}
