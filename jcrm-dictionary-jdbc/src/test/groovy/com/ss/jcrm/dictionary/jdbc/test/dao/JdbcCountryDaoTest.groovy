package com.ss.jcrm.dictionary.jdbc.test.dao

import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired

class JdbcCountryDaoTest extends JdbcDictionarySpecification {

    @Autowired
    CountryDao countryDao

    def "should create and load a new country"() {

    }
}