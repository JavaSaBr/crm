package com.ss.jcrm.dictionary.jasync.test.helper

import com.github.jasync.sql.db.ConcreteConnection
import com.github.jasync.sql.db.pool.ConnectionPool
import crm.dao.exception.DuplicateObjectDaoException
import crm.dictionary.api.Country
import crm.dictionary.api.DictionaryDbTestHelper
import crm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import com.ss.jcrm.integration.test.db.jasync.JAsyncTestHelper

class JAsyncDictionaryDbTestHelper extends JAsyncTestHelper implements DictionaryDbTestHelper {

    private final CountryDao countryDao
  
  JAsyncDictionaryDbTestHelper(
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

    void clearAllData() {
        JAsyncDictionarySpecification.clearAllTables(connectionPool, schema)
    }
    
    static def nextUID() {
        return String.valueOf(System.nanoTime() + Thread.currentThread().id)
    }
}
