package com.ss.jcrm.dictionary.jdbc.test.dao

import com.ss.jcrm.dao.exception.DuplicateObjectDaoException
import com.ss.jcrm.dictionary.api.dao.IndustryDao
import com.ss.jcrm.dictionary.jdbc.test.JdbcDictionarySpecification
import org.springframework.beans.factory.annotation.Autowired

import java.util.concurrent.CompletionException

class JdbcIndustryDaoTest extends JdbcDictionarySpecification {

    @Autowired
    IndustryDao industryDao

    def "should create and load a new industry"() {

        when:
            def industry = industryDao.create("testindustry")
        then:
            industry != null
            industry.getId() > 0
            industry.getName() == "testindustry"
    }

    def "should create and load a new industry using async"() {

        when:
            def industry = industryDao.createAsync("testindustry").join()
        then:
            industry != null
            industry.getId() > 0
            industry.getName() == "testindustry"
    }

    def "should prevent creating an industry with the same name"() {

        given:
            industryDao.create("testindustry")
        when:
            industryDao.create("testindustry")
        then:
            thrown DuplicateObjectDaoException
    }

    def "should prevent creating an industry with the same name using async"() {

        given:
            industryDao.createAsync("testindustry").join()
        when:
            industryDao.createAsync("testindustry").join()
        then:
            def ex = thrown(CompletionException)
            ex.getCause() instanceof DuplicateObjectDaoException
    }

    def "should load correctly created industries"() {

        given:
            industryDao.create("testindustry1")
            industryDao.create("testindustry2")
            industryDao.create("testindustry3")
        when:
            def result = industryDao.findAll()
        then:
            result.size() == 3
        when:
            def loaded = industryDao.findByName("testindustry2")
        then:
            loaded != null
            loaded.getId() > 0
        when:
            def reloaded = industryDao.findById(loaded.getId())
        then:
            reloaded != null
            reloaded.getId() == loaded.getId()
    }
}