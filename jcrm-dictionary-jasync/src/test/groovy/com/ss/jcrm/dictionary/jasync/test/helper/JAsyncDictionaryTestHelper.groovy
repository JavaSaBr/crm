package com.ss.jcrm.dictionary.jasync.test.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.Country
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper

import java.util.concurrent.CompletionException

import static com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification.clearAllTables

class JAsyncDictionaryTestHelper implements DictionaryTestHelper {

    private final ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool
    private final CountryDao countryDao
    private final String schema
    
    JAsyncDictionaryTestHelper(
        ConnectionPool<? extends ConcreteConnection> dictionaryConnectionPool,
        CountryDao countryDao,
        String schema
    ) {
        this.dictionaryConnectionPool = dictionaryConnectionPool
        this.countryDao = countryDao
        this.schema = schema
    }
    
    @Override
    Country newCountry() {
    
        for (def i = 0; i < 3; i++) {
            try {
                return newCountry(nextUID(), "none", "none")
            } catch (CompletionException e) {
                if (!e.getCause() instanceof DuplicateObjectDaoException) {
                    throw e
                }
            }
        }
    
        return newCountry(nextUID(), "none", "none")
    }
    
    @Override
    Country newCountry(String name) {
        return newCountry(name, "none", "none")
    }

    @Override
    Country newCountry(String name, String flagCode, String phoneCode) {
        return countryDao.create(name, flagCode, phoneCode).block()
    }

    @Override
    void clearAllData() {
        clearAllTables(dictionaryConnectionPool, schema)
    }
    
    static def nextUID() {
        return String.valueOf(System.nanoTime() + Thread.currentThread().id)
    }
}
