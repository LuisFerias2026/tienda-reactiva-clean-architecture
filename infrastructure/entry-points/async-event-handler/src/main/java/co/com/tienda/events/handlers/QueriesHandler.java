package co.com.tienda.events.handlers;

import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.jackson.JsonCloudEventData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableQueryListeners;
import reactor.core.publisher.Mono;
import lombok.extern.java.Log;
import java.util.logging.Level;
import io.cloudevents.CloudEvent;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@Log
@RequiredArgsConstructor
@EnableQueryListeners
public class QueriesHandler {
    private final JsonMapper mapper;


    public Mono<CloudEvent> handleQueryA(CloudEvent query) {
        log.log(Level.INFO, "Query received -> {0}", query);
        Object replyData = "Response Data";
        CloudEvent reply = CloudEventBuilder.v1() //
                .withId(UUID.randomUUID().toString()) //
                .withSource(URI.create("https://spring.io/foos"))//
                .withType("SOME_QUERY_REPLY") //
                .withTime(OffsetDateTime.now())
                .withData("application/json", JsonCloudEventData.wrap(mapper.valueToTree(replyData)))
                .build();
        return Mono.just(reply);
    }


}
