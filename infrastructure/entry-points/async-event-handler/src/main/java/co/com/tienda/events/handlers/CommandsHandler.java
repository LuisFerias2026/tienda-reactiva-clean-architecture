package co.com.tienda.events.handlers;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;
import lombok.extern.java.Log;
import java.util.logging.Level;
import io.cloudevents.CloudEvent;

@Log
@RequiredArgsConstructor
@EnableCommandListeners
public class CommandsHandler {

    public Mono<Void> handleCommandA(CloudEvent command) {
    log.log(Level.INFO, "Command received: {0} -> {1}", new Object[]{command.getType(), command.getData()});
        return Mono.empty();
    }

}
