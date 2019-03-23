package com.ss.jcrm.dictionary.jdbc.test.helper

import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecification

import javax.sql.DataSource

class DictionaryTestHelper {

    private final DataSource dictionaryDataSource
    private final CountryDao countryDao

    DictionaryTestHelper(DataSource dictionaryDataSource, CountryDao countryDao) {
        this.dictionaryDataSource = dictionaryDataSource
        this.countryDao = countryDao
    }

    def newCountry() {
        def name = String.valueOf(System.currentTimeMillis() + Thread.currentThread().id)
        return newCountry(name, "none", "none")
    }

    def newCountry(String name) {
        return newCountry(name, "none", "none")
    }

    def newCountry(String name, String flagCode, String phoneCode) {
        return countryDao.create(name, flagCode, phoneCode)
    }

    def clearAllData() {
        JdbcDictionarySpecification.clearAllTables(dictionaryDataSource)
    }
}
