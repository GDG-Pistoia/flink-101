package it.gdgpistoia.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.gdgpistoia.kafka.SubscriptionProducer;
import it.gdgpistoia.model.Subscription;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Slf4j
public class SubscriptionControl {

    @Inject
    SubscriptionProducer producer;

    public void send(Subscription subscription){
        try {
            producer.sentRecordToKafka(subscription);
        } catch (JsonProcessingException e) {
            log.info("operationName=sendSubscriptionToKafka message={}", e.getMessage());
        }
    }
}
