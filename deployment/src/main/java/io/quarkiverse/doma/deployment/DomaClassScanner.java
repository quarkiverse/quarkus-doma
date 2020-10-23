package io.quarkiverse.doma.deployment;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.logging.Logger;
import org.seasar.doma.DomainTypeImplementation;
import org.seasar.doma.Embeddable;
import org.seasar.doma.EmbeddableTypeImplementation;
import org.seasar.doma.Entity;
import org.seasar.doma.EntityTypeImplementation;

public class DomaClassScanner {

    private static final Logger logger = Logger.getLogger(DomaClassScanner.class);

    private static final DotName ENTITY = DotName.createSimple(Entity.class.getName());
    private static final DotName EMBEDDABLE = DotName.createSimple(Embeddable.class.getName());
    private static final DotName ENTITY_TYPE_IMPLEMENTATION = DotName.createSimple(EntityTypeImplementation.class.getName());
    private static final DotName EMBEDDABLE_TYPE_IMPLEMENTATION = DotName
            .createSimple(EmbeddableTypeImplementation.class.getName());
    private static final DotName DOMAIN_TYPE_IMPLEMENTATION = DotName.createSimple(DomainTypeImplementation.class.getName());

    private final IndexView indexView;

    DomaClassScanner(IndexView indexView) {
        this.indexView = Objects.requireNonNull(indexView);
    }

    List<String> scan() {
        return Stream.of(
                ENTITY,
                EMBEDDABLE,
                ENTITY_TYPE_IMPLEMENTATION,
                EMBEDDABLE_TYPE_IMPLEMENTATION,
                DOMAIN_TYPE_IMPLEMENTATION)
                .flatMap(it -> indexView.getAnnotations(it).stream())
                .map(AnnotationInstance::target)
                .map(AnnotationTarget::asClass)
                .map(ClassInfo::name)
                .map(DotName::toString)
                .peek(it -> logger.debugf("class found: %s", it))
                .collect(toList());
    }
}
