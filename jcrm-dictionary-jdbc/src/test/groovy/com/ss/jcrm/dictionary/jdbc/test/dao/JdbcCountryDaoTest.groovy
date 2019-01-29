package com.ss.jcrm.dictionary.jdbc.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletionException

class JdbcCountryDaoTest extends JdbcDictionarySpecification {

    @Autowired
    CountryDao countryDao

    def "should create and load a new country"() {

        when:
            def country = countryDao.create("testcountry", "testflag", "testphone")
        then:
            country != null
            country.getId() > 0
            country.getName() == "testcountry"
            country.getFlagCode() == "testflag"
            country.getPhoneCode() == "testphone"
    }

    def "should create and load a new country using async"() {

        when:
            def country = countryDao.createAsync("testcountry", "testflag", "testphone").join()
        then:
            country != null
            country.getId() > 0
            country.getName() == "testcountry"
            country.getFlagCode() == "testflag"
            country.getPhoneCode() == "testphone"
    }

    def "should prevent creating a country with the same name"() {

        given:
            countryDao.create("testcountry", "testflag", "testphone")
        when:
            countryDao.create("testcountry", "testflag", "testphone")
        then:
            thrown DuplicateObjectDaoException
    }

    def "should prevent creating a country with the same name using async"() {

        given:
            countryDao.createAsync("testcountry", "testflag", "testphone").join()
        when:
            countryDao.createAsync("testcountry", "testflag", "testphone").join()
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof DuplicateObjectDaoException
    }
}