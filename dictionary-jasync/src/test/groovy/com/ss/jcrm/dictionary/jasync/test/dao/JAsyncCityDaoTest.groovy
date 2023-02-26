package com.ss.jcrm.dictionary.jasync.test.dao

import crm.dao.exception.DuplicateObjectDaoException
import crm.dictionary.api.dao.CityDao
import crm.dictionary.jasync.JAsyncDictionarySpecification

import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class JAsyncCityDaoTest extends JAsyncDictionarySpecification {
  
  @Autowired
  CityDao cityDao
  
  def "should create and load new city"() {
    given:
        def country = dictionaryTestHelper.newCountry()
    when:
        def city = cityDao.create("testcity", country).block()
    then:
        city != null
        city.id() > 0
        city.name() == "testcity"
        city.country() != null
        city.country().id() == country.id()
  }
  
  def "should prevent creating city with the same name and the same country"() {
    given:
        def country = dictionaryTestHelper.newCountry()
        cityDao.create("testcity", country).block()
    when:
        cityDao.create("testcity", country).block()
    then:
        thrown DuplicateObjectDaoException
  }
  
  def "should load correctly created cities"() {
    given:
        
        def country1 = dictionaryTestHelper.newCountry(dictionaryTestHelper.nextCountryName())
        def country2 = dictionaryTestHelper.newCountry(dictionaryTestHelper.nextCountryName())
        def cityName1 = "JAsyncCityDaoTest_testcity1"
        def cityName2 = "JAsyncCityDaoTest_testcity2"
    
        Flux
            .concat(
                cityDao.create(cityName1, country1),
                cityDao.create(cityName2, country1),
                cityDao.create(cityName1, country2),
                cityDao.create(cityName2, country2))
            .blockLast()
    
    when:
        def result = waitForResults(cityDao.findAll())
    then:
        result.size() >= 4
    when:
        def loaded = cityDao.findByName(cityName2).block()
    then:
        loaded != null
        loaded.id() > 0
    when:
        def reloaded = cityDao.findById(loaded.id()).block()
    then:
        reloaded != null
        reloaded.id() == loaded.id()
  }
}
