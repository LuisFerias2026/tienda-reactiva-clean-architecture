package co.com.tienda.events.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;


import java.util.UUID;

class CommandsHandlerTest {
    private CommandsHandler commandsHandler;

    @BeforeEach
    void setUp() {
        commandsHandler = new CommandsHandler();
    }

    @Test
    void handleCommandATest() {
        ObjectMapper objectMapper = new ObjectMapper();
        CloudEvent command = CloudEventBuilder.v1() //
                .withId(UUID.randomUUID().toString()) //
                .withSource(URI.create("https://spring.io/foos"))//
                .withType("command")
                .withData("application/json", objectMapper.writeValueAsBytes("Data"))
                .build();
        StepVerifier.create(commandsHandler.handleCommandA(command)).expectComplete().verify();
    }
}
