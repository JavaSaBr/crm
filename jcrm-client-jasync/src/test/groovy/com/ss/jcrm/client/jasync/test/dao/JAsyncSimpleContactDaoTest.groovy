package com.ss.jcrm.client.jasync.test.dao

import com.ss.jcrm.client.api.ContactEmail
import com.ss.jcrm.client.api.ContactMessenger
import com.ss.jcrm.client.api.ContactPhoneNumber
import com.ss.jcrm.client.api.ContactSite
import com.ss.jcrm.client.api.EmailType
import com.ss.jcrm.client.api.MessengerType
import com.ss.jcrm.client.api.PhoneNumberType
import com.ss.jcrm.client.api.SiteType
import com.ss.jcrm.client.api.dao.SimpleContactDao
import com.ss.jcrm.client.api.impl.DefaultContactEmail
import com.ss.jcrm.client.api.impl.DefaultContactMessenger
import com.ss.jcrm.client.api.impl.DefaultContactPhoneNumber
import com.ss.jcrm.client.api.impl.DefaultContactSite
import com.ss.jcrm.client.jasync.test.JAsyncClientSpecification
import com.ss.jcrm.jasync.util.JAsyncUtils
import com.ss.jcrm.user.api.User
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class JAsyncSimpleContactDaoTest extends JAsyncClientSpecification {
    
    @Autowired
    SimpleContactDao simpleContactDao
    
    def "should create and load a simple contact"() {
        
        given:
            def assigner = userTestHelper.newUser("Test1")
            User[] curators = [
                userTestHelper.newUser("Curator1"),
                userTestHelper.newUser("Curator2")
            ]
            ContactPhoneNumber[] phoneNumbers = [
                new DefaultContactPhoneNumber("+7", "123", "456789", PhoneNumberType.WORK),
                new DefaultContactPhoneNumber("+7", "123", "098765", PhoneNumberType.HOME)
            ]
            ContactEmail[] emails = [
                new DefaultContactEmail("test@test.com", EmailType.WORK),
                new DefaultContactEmail("test2@test2.com", EmailType.HOME),
            ]
            ContactSite[] sites = [
                new DefaultContactSite("google.com", SiteType.HOME),
            ]
            ContactMessenger[] messengers = [
                new DefaultContactMessenger("user1", MessengerType.SKYPE),
                new DefaultContactMessenger("user2", MessengerType.TELEGRAM),
            ]
            def birthday = LocalDate.of(1900, 3, 12)
        when:
            def contact = simpleContactDao.create(
                assigner,
                curators,
                assigner.getOrganization(),
                "First name",
                "Second name",
                "Third name",
                birthday,
                phoneNumbers,
                emails,
                sites,
                messengers,
                "My company"
            ).block()
        then:
            contact != null
            contact.id > 0
            contact.assignerId == assigner.id
            contact.curatorIds == JAsyncUtils.toIds(curators)
            contact.organizationId == assigner.organization.id
            contact.firstName == "First name"
            contact.secondName == "Second name"
            contact.thirdName == "Third name"
            contact.birthday == birthday
            contact.phoneNumbers == phoneNumbers
            contact.emails == emails
            contact.sites == sites
            contact.messengers == messengers
            contact.company == "My company"
        when:
            contact = simpleContactDao.findById(contact.id).block()
        then:
            contact != null
            contact.id > 0
            contact.assignerId == assigner.id
            contact.curatorIds == JAsyncUtils.toIds(curators)
            contact.organizationId == assigner.organization.id
            contact.firstName == "First name"
            contact.secondName == "Second name"
            contact.thirdName == "Third name"
            contact.birthday == birthday
            contact.phoneNumbers == phoneNumbers
            contact.emails == emails
            contact.sites == sites
            contact.messengers == messengers
            contact.company == "My company"
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
