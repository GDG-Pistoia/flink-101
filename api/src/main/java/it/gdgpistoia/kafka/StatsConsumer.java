package it.gdgpistoia.kafka;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
@Slf4j
public class StatsConsumer {

    @Incoming("stats-in")
    @Outgoing("stream-messages")
    public String getStatsByKafka(String msg){
        log.info("operationName=getStatsByKafka msg={}", msg);
        return msg;
    }
}
