package co.com.tienda.events.handlers;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import tools.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class QueriesHandlerTest {
    private QueriesHandler queriesHandler;

    @BeforeEach
    void setUp() {
        queriesHandler = new QueriesHandler(new JsonMapper());
    }

    @Test
    void queriesHandlerTest() {
        JsonMapper objectMapper = new JsonMapper();
        CloudEvent query = CloudEventBuilder.v1() //
                .withId(UUID.randomUUID().toString()) //
                .withSource(URI.create("https://spring.io/foos"))//
                .withType("query")
                .withData("application/json", objectMapper.writeValueAsBytes("Data"))
                .build();
        StepVerifier.create(queriesHandler.handleQueryA(query))
                .expectNextMatches(response ->
                        response != null &&
                        "SOME_QUERY_REPLY".equals(response.getType()) &&
                        response.getData() != null
                )
                .verifyComplete();
    }
}
