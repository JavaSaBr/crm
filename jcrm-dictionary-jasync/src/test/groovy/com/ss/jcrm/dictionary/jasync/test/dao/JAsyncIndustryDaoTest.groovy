package com.ss.jcrm.dictionary.jasync.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.IndustryDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class JAsyncIndustryDaoTest extends JAsyncDictionarySpecification {

    @Autowired
    IndustryDao industryDao

    def "should create and load new industry"() {

        when:
            def industry = industryDao.create("testindustry").block()
        then:
            industry != null
            industry.id() > 0
            industry.name() == "testindustry"
    }
    
    def "should prevent creating industry with the same name"() {

        given:
            industryDao.create("testindustry").block()
        when:
            industryDao.create("testindustry").block()
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
            def result = industryDao.findAll().block()
        then:
            result.size() == 3
        when:
            def loaded = industryDao.findByName("testindustry2").block()
        then:
            loaded != null
            loaded.id() > 0
        when:
            def reloaded = industryDao.findById(loaded.id()).block()
        then:
            reloaded != null
            reloaded.id() == loaded.id()
    }
}
