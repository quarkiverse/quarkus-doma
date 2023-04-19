package io.quarkiverse.doma.deployment.model;

import java.util.List;
import java.util.Objects;

import jakarta.enterprise.context.ApplicationScoped;

import org.seasar.doma.jdbc.criteria.Entityql;

@ApplicationScoped
public class MessageRepository {

    private final Entityql entityql;
    private final MessageDao messageDao;

    public MessageRepository(Entityql entityql, MessageDao messageDao) {
        this.entityql = Objects.requireNonNull(entityql);
        this.messageDao = Objects.requireNonNull(messageDao);
    }

    public Message selectById(int id) {
        return messageDao.selectById(id);
    }

    public List<Message> selectByLocale(Locale locale) {
        Message_ m = new Message_();
        return entityql
                .from(m)
                .where(
                        c -> {
                            c.eq(m.locale.language, locale.language);
                            c.eq(m.locale.country, locale.country);
                        })
                .fetch();
    }

    public void insert(Message message) {
        Message_ m = new Message_();
        entityql.insert(m, message).execute();
    }
}
