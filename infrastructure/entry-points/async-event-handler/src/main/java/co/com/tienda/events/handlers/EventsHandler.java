package co.com.tienda.events.handlers;

import co.com.tienda.model.product.Product;
import co.com.tienda.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import lombok.extern.java.Log;
import java.util.logging.Level;
import io.cloudevents.CloudEvent;
import tools.jackson.databind.json.JsonMapper;

@Log
@RequiredArgsConstructor
@EnableEventListeners
public class EventsHandler {
    public static final String PRODUCT_CREATE_COMMAND = "product.create";
    private final ProductUseCase productUseCase;
    private final JsonMapper mapper;

    public Mono<Void> handleProductCreated(CloudEvent event) {
        log.log(Level.INFO, "Product event received: {0}", event.getType());
        return Mono.fromCallable(() -> mapper.readValue(event.getData().toBytes(), Product.class))
                .flatMap(productUseCase::create)
                .then();
    }

}
