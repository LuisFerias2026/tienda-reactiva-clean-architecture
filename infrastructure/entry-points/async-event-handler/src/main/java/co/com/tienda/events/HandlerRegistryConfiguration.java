package co.com.tienda.events;
import co.com.tienda.events.handlers.CommandsHandler;
import co.com.tienda.events.handlers.EventsHandler;
import co.com.tienda.events.handlers.QueriesHandler;
import co.com.tienda.events.handlers.NotificationsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.com.tienda.events.handlers.EventsHandler.PRODUCT_CREATE_COMMAND;

@Configuration
public class HandlerRegistryConfiguration {

    // see more at: https://reactivecommons.org/reactive-commons-java/#_handlerregistry_2
    @Bean
    public HandlerRegistry handlerRegistry(CommandsHandler commands, EventsHandler events, QueriesHandler queries,
    NotificationsHandler notification) {
         return HandlerRegistry.register()
                        .listenNotificationCloudEvent("some.broadcast.event.name", notification::handleNotificationA)
                        .listenCloudEvent(PRODUCT_CREATE_COMMAND, events::handleProductCreated)
                        .handleCloudEventCommand("some.command.name", commands::handleCommandA)
                        .serveCloudEventQuery("some.query.name", queries::handleQueryA);
    }
}
