package com.ss.jcrm.dictionary.jasync.test.dao

import crm.dao.exception.DuplicateObjectDaoException
import crm.dictionary.api.dao.CountryDao
import crm.dictionary.jasync.JAsyncDictionarySpecification

import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class JAsyncCountryDaoTest extends JAsyncDictionarySpecification {
  
  @Autowired
  CountryDao countryDao
  
  def "should create and load new country"() {
    given:
        def countryName = dictionaryTestHelper.nextCountryName()
    when:
        def country = countryDao
            .create(countryName, "testflag", "testphone")
            .block()
    then:
        country != null
        country.id() > 0
        country.name() == countryName
        country.flagCode() == "testflag"
        country.phoneCode() == "testphone"
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
    
        def countryName1 = dictionaryTestHelper.nextCountryName()
        def countryName2 = dictionaryTestHelper.nextCountryName()
        def countryName3 = dictionaryTestHelper.nextCountryName()
    
        Flux
            .concat(
                countryDao.create(countryName1, "testflag1", "testphone1"),
                countryDao.create(countryName2, "testflag2", "testphone2"),
                countryDao.create(countryName3, "testflag3", "testphone3")
            )
            .blockLast()
    
    when:
        def result = waitForResults(countryDao.findAll())
    then:
        result.size() >= 3
    when:
        def loaded = countryDao.findByName(countryName2).block()
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
