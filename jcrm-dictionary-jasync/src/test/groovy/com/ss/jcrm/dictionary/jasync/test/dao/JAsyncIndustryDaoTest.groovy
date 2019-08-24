package com.ss.jcrm.dictionary.jasync.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.IndustryDao
import com.ss.jcrm.dictionary.jasync.test.JAsyncDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

import java.util.concurrent.CompletionException

class JAsyncIndustryDaoTest extends JAsyncDictionarySpecification {

    @Autowired
    IndustryDao industryDao

    def "should create and load a new industry"() {

        when:
            def industry = industryDao.create("testindustry").block()
        then:
            industry != null
            industry.getId() > 0
            industry.getName() == "testindustry"
    }
    
    def "should prevent creating an industry with the same name"() {

        given:
            industryDao.create("testindustry").block()
        when:
            industryDao.create("testindustry").block()
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof DuplicateObjectDaoException
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
            loaded.getId() > 0
        when:
            def reloaded = industryDao.findById(loaded.getId()).block()
        then:
            reloaded != null
            reloaded.getId() == loaded.getId()
    }
}
