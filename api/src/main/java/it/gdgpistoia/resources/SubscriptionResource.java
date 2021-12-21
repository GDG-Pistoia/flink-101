package it.gdgpistoia.resources;

import it.gdgpistoia.control.SubscriptionControl;
import it.gdgpistoia.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subscriptions")
@Slf4j
public class SubscriptionResource {

    @Inject
    SubscriptionControl control;

    @Inject
    @Channel("stream-messages")
    Publisher<String> statsMessage;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Subscription subscription) {
        control.send(subscription);
        return Response.ok().build();
    }

    @Path("/stats")
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<String> getStats() {
        return statsMessage;
    }

}