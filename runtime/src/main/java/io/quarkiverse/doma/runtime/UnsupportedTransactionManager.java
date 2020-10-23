package io.quarkiverse.doma.runtime;

import java.util.function.Supplier;

import javax.inject.Singleton;

import org.seasar.doma.jdbc.tx.TransactionIsolationLevel;
import org.seasar.doma.jdbc.tx.TransactionManager;

import io.quarkus.arc.DefaultBean;

@Singleton
@DefaultBean
public class UnsupportedTransactionManager implements TransactionManager {

    @Override
    public void required(Runnable block) {
        throw useNarayanaJta();
    }

    @Override
    public void required(TransactionIsolationLevel isolationLevel, Runnable block) {
        throw useNarayanaJta();
    }

    @Override
    public <RESULT> RESULT required(Supplier<RESULT> supplier) {
        throw useNarayanaJta();
    }

    @Override
    public <RESULT> RESULT required(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        throw useNarayanaJta();
    }

    @Override
    public void requiresNew(Runnable block) {
        throw useNarayanaJta();
    }

    @Override
    public void requiresNew(TransactionIsolationLevel isolationLevel, Runnable block) {
        throw useNarayanaJta();
    }

    @Override
    public <RESULT> RESULT requiresNew(Supplier<RESULT> supplier) {
        throw useNarayanaJta();
    }

    @Override
    public <RESULT> RESULT requiresNew(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        throw useNarayanaJta();
    }

    @Override
    public void notSupported(Runnable block) {
        throw useNarayanaJta();
    }

    @Override
    public void notSupported(TransactionIsolationLevel isolationLevel, Runnable block) {
        throw useNarayanaJta();
    }

    @Override
    public <RESULT> RESULT notSupported(Supplier<RESULT> supplier) {
        throw useNarayanaJta();
    }

    @Override
    public <RESULT> RESULT notSupported(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        throw useNarayanaJta();
    }

    @Override
    public void setRollbackOnly() {
        throw useNarayanaJta();
    }

    @Override
    public boolean isRollbackOnly() {
        throw useNarayanaJta();
    }

    @Override
    public void setSavepoint(String savepointName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasSavepoint(String savepointName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void releaseSavepoint(String savepointName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback(String savepointName) {
        throw new UnsupportedOperationException();
    }

    private UnsupportedOperationException useNarayanaJta() {
        return new UnsupportedOperationException(
                "Use quarkus-narayana-jta directly. See https://quarkus.io/guides/transaction");
    }
}
