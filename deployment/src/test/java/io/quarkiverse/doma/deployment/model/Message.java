package io.quarkiverse.doma.deployment.model;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel)
public class Message {

    @Id
    public Integer id;
    public Text text;
    public Locale locale;

    public Message() {
    }

    public Message(Integer id, String text, Locale locale) {
        this.id = id;
        this.text = new Text(text);
        this.locale = locale;
    }
}
