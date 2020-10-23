package io.quarkiverse.doma.runtime;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.seasar.doma.jdbc.RequiresNewController;

import io.quarkus.arc.DefaultBean;

@Singleton
@DefaultBean
public class JtaRequiresNewController implements RequiresNewController {

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public <R> R requiresNew(Callback<R> callback) throws Throwable {
        return RequiresNewController.super.requiresNew(callback);
    }
}
