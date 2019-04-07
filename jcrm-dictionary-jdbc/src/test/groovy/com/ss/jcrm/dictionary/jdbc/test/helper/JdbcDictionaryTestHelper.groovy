package com.ss.jcrm.dictionary.jdbc.test.helper

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.Country
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecification

import javax.sql.DataSource

class JdbcDictionaryTestHelper implements DictionaryTestHelper {

    private final DataSource dictionaryDataSource
    private final CountryDao countryDao

    JdbcDictionaryTestHelper(DataSource dictionaryDataSource, CountryDao countryDao) {
        this.dictionaryDataSource = dictionaryDataSource
        this.countryDao = countryDao
    }
    
    @Override
    Country newCountry() {
        
        for (def i = 0; i <3; i++) {
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
        return countryDao.create(name, flagCode, phoneCode)
    }

    @Override
    void clearAllData() {
        JdbcDictionarySpecification.clearAllTables(dictionaryDataSource)
    }
    
    static def nextUID() {
        return String.valueOf(System.nanoTime() + Thread.currentThread().id)
    }
}
