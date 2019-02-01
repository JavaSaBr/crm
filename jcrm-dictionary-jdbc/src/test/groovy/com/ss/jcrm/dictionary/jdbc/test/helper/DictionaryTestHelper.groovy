package com.ss.jcrm.dictionary.jdbc.test.helper

import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecification

import javax.sql.DataSource

class DictionaryTestHelper {

    class TestCountry {

        String name
        String flagCode
        String phoneCode

        long id

        TestCountry(String name, String flagCode, String phoneCode, long id) {
            this.name = name
            this.flagCode = flagCode
            this.phoneCode = phoneCode
            this.id = id
        }
    }

    private final DataSource dictionaryDataSource
    private final CountryDao countryDao

    DictionaryTestHelper(DataSource dictionaryDataSource, CountryDao countryDao) {
        this.dictionaryDataSource = dictionaryDataSource
        this.countryDao = countryDao
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
