package co.com.tienda.events.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import tools.jackson.databind.json.JsonMapper;
import java.net.URI;

import java.util.UUID;

class NotificationsHandlerTest {
    private NotificationsHandler notificationsHandler ;

    @BeforeEach
    void setUp() {
        notificationsHandler = new NotificationsHandler();
    }

    @Test
    void handleNotificationATest() {
        JsonMapper objectMapper = new JsonMapper();
        CloudEvent event = CloudEventBuilder.v1() //
                .withId(UUID.randomUUID().toString()) //
                .withSource(URI.create("https://spring.io/foos"))//
                .withType("notificacion")//
                .withData("application/json", objectMapper.writeValueAsBytes("Data"))
                .build();
        StepVerifier.create(notificationsHandler.handleNotificationA(event)).expectComplete().verify();
    }
}
