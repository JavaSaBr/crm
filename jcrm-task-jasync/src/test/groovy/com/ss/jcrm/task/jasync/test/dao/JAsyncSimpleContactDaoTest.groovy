package com.ss.jcrm.task.jasync.test.dao

import com.ss.jcrm.task.api.dao.SimpleContactDao
import com.ss.jcrm.task.jasync.test.JAsyncTaskSpecification
import org.springframework.beans.factory.annotation.Autowired

class JAsyncSimpleContactDaoTest extends JAsyncTaskSpecification {
    
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
            def contact = taskTestHelper.newSimpleContact()
        when:
            def loaded = simpleContactDao.findByIdAsync(contact.id).join()
        then:
            loaded != null
            loaded.id == contact.id
            loaded.organizationId == contact.id
            loaded.firstName == contact.firstName
            loaded.secondName == loaded.secondName
            loaded.thirdName == loaded.thirdName
    }
}
