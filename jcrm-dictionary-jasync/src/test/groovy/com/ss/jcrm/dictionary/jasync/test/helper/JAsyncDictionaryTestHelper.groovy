package com.ss.jcrm.dictionary.jasync.test.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.Country
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.db.jasync.JAsyncTestHelper

import java.util.concurrent.CompletionException

import static com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification.clearAllTables

class JAsyncDictionaryTestHelper extends JAsyncTestHelper implements DictionaryTestHelper {

    private final CountryDao countryDao
    
    JAsyncDictionaryTestHelper(
        ConnectionPool<? extends ConcreteConnection> connectionPool,
        CountryDao countryDao,
        String schema
    ) {
        super(connectionPool, schema)
        this.countryDao = countryDao
    }
    
    @Override
    Country newCountry() {
    
        for (def i = 0; i < 3; i++) {
            try {
                return newCountry(nextUID(), "none", "none")
            } catch (DuplicateObjectDaoException e) {
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
        clearAllTables(connectionPool, schema)
    }
    
    static def nextUID() {
        return String.valueOf(System.nanoTime() + Thread.currentThread().id)
    }
}
