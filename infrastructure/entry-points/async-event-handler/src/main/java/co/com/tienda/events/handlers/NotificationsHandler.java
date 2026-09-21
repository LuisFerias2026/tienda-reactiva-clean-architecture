package co.com.tienda.events.handlers;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableNotificationListener;
import reactor.core.publisher.Mono;
import lombok.extern.java.Log;
import java.util.logging.Level;
import io.cloudevents.CloudEvent;

@Log
@RequiredArgsConstructor
@EnableNotificationListener
public class NotificationsHandler {

    public Mono<Void> handleNotificationA(CloudEvent event) {
    log.log(Level.INFO, "Event received: {0} -> {1}", new Object[]{event.getType(), event.getData()});
        return Mono.empty();
    }

}
