package it.gdgpistoia.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.Record;
import it.gdgpistoia.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class SubscriptionProducer {

    @Inject
    @Channel("subscriptions-out")
    Emitter<Record<String, String>> emitter;

    @Inject
    ObjectMapper objectMapper;

    public void sentRecordToKafka(Subscription subscription) throws JsonProcessingException {
        log.info("operationName=sentRecordToKafka subscription={}", subscription);
        emitter.send(Record.of(subscription.getUuid(), objectMapper.writeValueAsString(subscription.getCmd())));
    }
}
