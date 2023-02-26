package com.ss.jcrm.dictionary.jasync.test.dao

import crm.dao.exception.DuplicateObjectDaoException
import crm.dictionary.api.dao.IndustryDao
import crm.dictionary.jasync.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class JAsyncIndustryDaoTest extends JAsyncDictionarySpecification {
  
  @Autowired
  IndustryDao industryDao
  
  def "should create and load new industry"() {
    given:
        def industryName = dictionaryTestHelper.nextIndustryName()
    when:
        def industry = waitForResult(industryDao.create(industryName))
    then:
        industry != null
        industry.id() > 0
        industry.name() == industryName
  }
  
  def "should prevent creating industry with the same name"() {
    given:
        def industryName = dictionaryTestHelper.nextIndustryName()
        waitForResult(industryDao.create(industryName))
    when:
        waitForResult(industryDao.create(industryName))
    then:
        thrown DuplicateObjectDaoException
  }
  
  
  def "should load correctly created industries"() {
    given:
        def industryName1 = dictionaryTestHelper.nextIndustryName()
        def industryName2 = dictionaryTestHelper.nextIndustryName()
        def industryName3 = dictionaryTestHelper.nextIndustryName()
    
        Flux
            .concat(
                industryDao.create(industryName1),
                industryDao.create(industryName2),
                industryDao.create(industryName3)
            )
            .blockLast()
    when:
        def result = waitForResults(industryDao.findAll())
    then:
        result.size() >= 3
    when:
        def loaded = waitForResult(industryDao.findByName(industryName2))
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
