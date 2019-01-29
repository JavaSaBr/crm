package com.ss.jcrm.dictionary.jdbc.test.help

import com.ss.jcrm.dictionary.api.dao.CountryDao

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

    private final CountryDao countryDao

    DictionaryTestHelper(CountryDao countryDao) {
        this.countryDao = countryDao
    }

    def newDaoCountry(String name) {
        return newDaoCountry(name, "nonflagcode", "nonphonecode")
    }

    def newDaoCountry(String name, String flagCode, String phoneCode) {
        return countryDao.create(name, flagCode, phoneCode)
    }
}
