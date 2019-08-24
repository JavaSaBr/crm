package com.ss.jcrm.dictionary.jasync.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.CityDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

import java.util.concurrent.CompletionException

class JAsyncCityDaoTest extends JAsyncDictionarySpecification {

    @Autowired
    CityDao cityDao

    def "should create and load a new city"() {

        given:
            def country = dictionaryTestHelper.newCountry("testcountry")
        when:
            def city = cityDao.create("testcity", country).block()
        then:
            city != null
            city.getId() > 0
            city.getName() == "testcity"
            city.getCountry() != null
            city.getCountry().getId() == country.getId()
    }
    
    def "should prevent creating a city with the same name and the same country"() {

        given:
            def country = dictionaryTestHelper.newCountry("testcountry")
            cityDao.create("testcity", country).block()
        when:
            cityDao.create("testcity", country).block()
        then:
            def e = thrown(CompletionException)
            e.getCause() instanceof DuplicateObjectDaoException
    }

    def "should load correctly created cities"() {

        given:
           
            def country1 = dictionaryTestHelper.newCountry("testcountry1")
            def country2 = dictionaryTestHelper.newCountry("testcountry2")
    
            Flux.concat(
                cityDao.create("testcity1", country1),
                cityDao.create("testcity2", country1),
                cityDao.create("testcity1", country2),
                cityDao.create("testcity2", country2)
            ).blockLast()
        
        when:
            def result = cityDao.findAll().block()
        then:
            result.size() == 4
        when:
            def loaded = cityDao.findByName("testcity2").block()
        then:
            loaded != null
            loaded.getId() > 0
        when:
            def reloaded = cityDao.findById(loaded.getId()).block()
        then:
            reloaded != null
            reloaded.getId() == loaded.getId()
    }
}
