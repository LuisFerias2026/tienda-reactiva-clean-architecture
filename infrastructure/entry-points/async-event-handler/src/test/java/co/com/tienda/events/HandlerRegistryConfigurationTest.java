package co.com.tienda.events;

import co.com.tienda.events.handlers.CommandsHandler;
import co.com.tienda.events.handlers.EventsHandler;
import co.com.tienda.events.handlers.QueriesHandler;
import co.com.tienda.events.handlers.NotificationsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.async.api.HandlerRegistry;
import tools.jackson.databind.json.JsonMapper;
import co.com.tienda.usecase.product.ProductUseCase;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HandlerRegistryConfigurationTest {

    EventsHandler eventsHandler;
    CommandsHandler commandsHandler;
    QueriesHandler queriesHandler;
    NotificationsHandler notificationHandler;

    @BeforeEach
    void setUp() {
        eventsHandler = new EventsHandler(mock(ProductUseCase.class), new JsonMapper());
        commandsHandler = new CommandsHandler();
        queriesHandler = new QueriesHandler(new JsonMapper());
        notificationHandler = new NotificationsHandler();
    }

    @Test
    void testHandlerRegistry() {
        HandlerRegistryConfiguration handlerRegistryConfiguration = new HandlerRegistryConfiguration();
        HandlerRegistry handlerRegistry = handlerRegistryConfiguration.handlerRegistry(commandsHandler, eventsHandler, queriesHandler,
        notificationHandler);

        assertNotNull(handlerRegistry);
    }
}
