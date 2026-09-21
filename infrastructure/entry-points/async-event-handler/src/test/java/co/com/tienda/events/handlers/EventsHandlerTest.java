package co.com.tienda.events.handlers;

import co.com.tienda.model.product.Product;
import co.com.tienda.usecase.product.ProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;

import java.util.UUID;

import static org.mockito.Mockito.when;

class EventsHandlerTest {
    private EventsHandler eventsHandler;
    @Mock
    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventsHandler = new EventsHandler(productUseCase, new tools.jackson.databind.json.JsonMapper());
    }

    @Test
    void handleEventATest() {
        ObjectMapper objectMapper = new ObjectMapper();
        CloudEvent event = CloudEventBuilder.v1() //
                .withId(UUID.randomUUID().toString()) //
                .withSource(URI.create("https://spring.io/foos"))//
                .withType(EventsHandler.PRODUCT_CREATE_COMMAND)
                .withData("application/json", objectMapper.writeValueAsBytes(Product.builder()
                    .id("1").name("Cafe").price(new java.math.BigDecimal("12500.00")).stock(10).build()))
                .build();
            when(productUseCase.create(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenReturn(reactor.core.publisher.Mono.just(Product.builder().id("1").name("Cafe").build()));
            StepVerifier.create(eventsHandler.handleProductCreated(event)).expectComplete().verify();
    }
}
