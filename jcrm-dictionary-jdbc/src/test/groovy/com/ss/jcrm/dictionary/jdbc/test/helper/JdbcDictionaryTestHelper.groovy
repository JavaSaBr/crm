package com.ss.jcrm.dictionary.jdbc.test.helper

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
        def name = String.valueOf(System.currentTimeMillis() + Thread.currentThread().id)
        return newCountry(name, "none", "none")
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
}
