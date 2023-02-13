package com.ss.jcrm.dictionary.jasync.test.dao

import crm.dao.exception.DuplicateObjectDaoException
import crm.dictionary.api.dao.IndustryDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class JAsyncIndustryDaoTest extends JAsyncDictionarySpecification {
  
  @Autowired
  IndustryDao industryDao
  
  def "should create and load new industry"() {
    
    when:
        def industry = waitForResult(industryDao.create("testindustry"))
    then:
        industry != null
        industry.id() > 0
        industry.name() == "testindustry"
  }
  
  def "should prevent creating industry with the same name"() {
    
    given:
        waitForResult(industryDao.create("testindustry"))
    when:
        waitForResult(industryDao.create("testindustry"))
    then:
        thrown DuplicateObjectDaoException
  }
  
  
  def "should load correctly created industries"() {
    
    given:
        
        Flux.concat(
            industryDao.create("testindustry1"),
            industryDao.create("testindustry2"),
            industryDao.create("testindustry3")
        ).blockLast()
    
    when:
        def result = waitForResults(industryDao.findAll())
    then:
        result.size() == 3
    when:
        def loaded = waitForResult(industryDao.findByName("testindustry2"))
    then:
        loaded != null
        loaded.id() > 0
    when:
        def reloaded = waitForResult(industryDao.findById(loaded.id()))
    then:
        reloaded != null
        reloaded.id() == loaded.id()
  }
}
