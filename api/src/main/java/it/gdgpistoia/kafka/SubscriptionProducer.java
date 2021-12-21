package it.gdgpistoia.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.kafka.Record;
import it.gdgpistoia.model.Subscription;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubscriptionProducer {

    @Inject
    @Channel("subscriptions-out")
    Emitter<Record<Integer, String>> emitter;

    @Inject
    ObjectMapper objectMapper;

    public void sendToKafka(Subscription subscription) throws JsonProcessingException {
        emitter.send(Record.of(subscription.getId(), objectMapper.writeValueAsString(subscription.getCmd())));
    }
}
