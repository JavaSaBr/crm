package com.ss.jcrm.dictionary.jasync.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletionException

class JAsyncCountryDaoTest extends JAsyncDictionarySpecification {

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

    def "should load correctly created countries"() {

        given:
            countryDao.create("testcountry1", "testflag1", "testphone1")
            countryDao.create("testcountry2", "testflag2", "testphone2")
            countryDao.create("testcountry3", "testflag3", "testphone3")
        when:
            def result = countryDao.findAll()
        then:
            result.size() == 3
        when:
            def loaded = countryDao.findByName("testcountry2")
        then:
            loaded != null
            loaded.getId() > 0
        when:
            def reloaded = countryDao.findById(loaded.getId())
        then:
            reloaded != null
            reloaded.getId() == loaded.getId()
    }
}
