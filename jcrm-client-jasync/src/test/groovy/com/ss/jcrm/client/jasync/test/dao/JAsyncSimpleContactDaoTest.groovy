package com.ss.jcrm.client.jasync.test.dao

import com.ss.jcrm.client.api.dao.SimpleContactDao
import com.ss.jcrm.client.jasync.test.JAsyncClientSpecification
import org.springframework.beans.factory.annotation.Autowired

class JAsyncSimpleContactDaoTest extends JAsyncClientSpecification {
    
    @Autowired
    SimpleContactDao simpleContactDao
    
    def "should create and load a simple contact"() {
        
        given:
            def org = userTestHelper.newOrg()
        when:
            def contact = simpleContactDao.create(
                org,
                "First name",
                "Second name",
                "Third name"
            ).block()
        then:
            contact != null
            contact.id > 0
            contact.organizationId == org.id
            contact.firstName == "First name"
            contact.secondName == "Second name"
            contact.thirdName == "Third name"
    }
    
    def "should load a simple contact"() {
        
        given:
            def contact = clientTestHelper.newSimpleContact()
        when:
            def loaded = simpleContactDao.findById(contact.id).block()
        then:
            loaded != null
            loaded.id == contact.id
            loaded.organizationId == contact.id
            loaded.firstName == contact.firstName
            loaded.secondName == loaded.secondName
            loaded.thirdName == loaded.thirdName
    }
    
    def "should load a simple contact only under its organization"() {
        
        given:
            def contact = clientTestHelper.newSimpleContact()
        when:
            def loaded = simpleContactDao.findByIdAndOrg(contact.id, contact.organizationId).block()
        then:
            loaded != null
            loaded.id == contact.id
            loaded.organizationId == contact.id
            loaded.firstName == contact.firstName
            loaded.secondName == loaded.secondName
            loaded.thirdName == loaded.thirdName
        when:
            loaded = simpleContactDao.findByIdAndOrg(contact.id, contact.organizationId + 1).block()
        then:
            loaded == null
    }
}
