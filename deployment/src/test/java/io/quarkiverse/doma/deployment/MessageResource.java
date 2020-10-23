package io.quarkiverse.doma.deployment;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkiverse.doma.deployment.model.Locale;
import io.quarkiverse.doma.deployment.model.Message;
import io.quarkiverse.doma.deployment.model.MessageRepository;

@Path("/hello")
public class MessageResource {

    private final MessageRepository messageRepository;

    public MessageResource(MessageRepository messageRepository) {
        this.messageRepository = Objects.requireNonNull(messageRepository);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        List<Message> messages = messageRepository.selectByLocale(new Locale("en", "US"));
        return messages.stream().findFirst().map(m -> m.text.getValue()).orElseGet(() -> "empty");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String hello(@PathParam("id") int id) {
        Message message = messageRepository.selectById(id);
        return message == null ? "empty" : message.text.getValue();
    }
}
