package com.ss.jcrm.dictionary.jasync.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.CountryDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class JAsyncCountryDaoTest extends JAsyncDictionarySpecification {

    @Autowired
    CountryDao countryDao

    def "should create and load new country"() {

        when:
            def country = countryDao.create("testcountry", "testflag", "testphone")
                .block()
        then:
            country != null
            country.id() > 0
            country.name() == "testcountry"
            country.getFlagCode() == "testflag"
            country.getPhoneCode() == "testphone"
    }
    
    def "should prevent creating country with the same name"() {

        given:
            countryDao.create("testcountry", "testflag", "testphone").block()
        when:
            countryDao.create("testcountry", "testflag", "testphone").block()
        then:
            thrown DuplicateObjectDaoException
    }
    
    def "should load correctly created countries"() {

        given:
    
            Flux.concat(
                countryDao.create("testcountry1", "testflag1", "testphone1"),
                countryDao.create("testcountry2", "testflag2", "testphone2"),
                countryDao.create("testcountry3", "testflag3", "testphone3")
            ).blockLast()
            
        when:
            def result = countryDao.findAll().block()
        then:
            result.size() == 3
        when:
            def loaded = countryDao.findByName("testcountry2").block()
        then:
            loaded != null
            loaded.id() > 0
        when:
            def reloaded = countryDao.findById(loaded.id()).block()
        then:
            reloaded != null
            reloaded.id() == loaded.id()
    }
}
