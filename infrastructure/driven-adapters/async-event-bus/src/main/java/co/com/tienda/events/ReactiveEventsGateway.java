package co.com.tienda.events;

import co.com.tienda.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.jackson.JsonCloudEventData;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.logging.Level;

import static reactor.core.publisher.Mono.from;

@Log
@RequiredArgsConstructor
@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String PRODUCT_CREATED_EVENT = "product.created";
    public static final String SOME_EVENT_NAME = PRODUCT_CREATED_EVENT;
    public static final String SOME_NOTIFICATION_NAME = "some.broadcast.event.name";

    private final DomainEventBus domainEventBus;
    private final JsonMapper mapper;

    @Override
    public Mono<Void> emit(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{PRODUCT_CREATED_EVENT, event.toString()});
        CloudEvent eventCloudEvent = CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("https://reactive-commons.org/foos"))
                .withType(PRODUCT_CREATED_EVENT)
                .withTime(OffsetDateTime.now())
                .withData("application/json", JsonCloudEventData.wrap(mapper.valueToTree(event)))
                .build();

         return from(domainEventBus.emit(eventCloudEvent));
    }

      @Override
      public Mono<Void> notify(Object event) {
          log.log(Level.INFO, "Sending domain notification: {0}: {1}", new String[]{SOME_NOTIFICATION_NAME, event.toString()});
          CloudEvent eventCloudEvent = CloudEventBuilder.v1()
                  .withId(UUID.randomUUID().toString())
                  .withSource(URI.create("https://reactive-commons.org/foos"))
                  .withType(SOME_NOTIFICATION_NAME)
                  .withTime(OffsetDateTime.now())
                  .withData("application/json", JsonCloudEventData.wrap(mapper.valueToTree(event)))
                  .build();

           return from(domainEventBus.emit(eventCloudEvent));
      }

}
