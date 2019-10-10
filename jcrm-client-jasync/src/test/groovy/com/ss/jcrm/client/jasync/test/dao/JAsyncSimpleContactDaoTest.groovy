package com.ss.jcrm.client.jasync.test.dao

import com.ss.jcrm.client.api.*
import com.ss.jcrm.client.api.dao.SimpleContactDao
import com.ss.jcrm.client.api.impl.DefaultContactEmail
import com.ss.jcrm.client.api.impl.DefaultContactMessenger
import com.ss.jcrm.client.api.impl.DefaultContactPhoneNumber
import com.ss.jcrm.client.api.impl.DefaultContactSite
import com.ss.jcrm.client.jasync.test.JAsyncClientSpecification
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.jasync.util.JAsyncUtils
import com.ss.jcrm.user.api.User
import com.ss.rlib.common.util.array.Array
import com.ss.rlib.common.util.array.ArrayFactory
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant
import java.time.LocalDate

class JAsyncSimpleContactDaoTest extends JAsyncClientSpecification {
    
    @Autowired
    SimpleContactDao simpleContactDao
    
    def "should create and load a simple contact"() {
        
        given:
            
            def assigner = userTestHelper.newUser("Test1")
            def currentTime = Instant.now()
        
            Array<User> curators = ArrayFactory.asArray(
                userTestHelper.newUser("Curator1"),
                userTestHelper.newUser("Curator2")
            )
            ContactPhoneNumber[] phoneNumbers = [
                new DefaultContactPhoneNumber("+7", "123", "456789", PhoneNumberType.WORK),
                new DefaultContactPhoneNumber("+7", "123", "098765", PhoneNumberType.MOBILE)
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
            contact.created.isAfter(currentTime)
            contact.modified.isAfter(currentTime)
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
            contact.created.isAfter(currentTime)
            contact.modified.isAfter(currentTime)
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
        when:
            loaded = simpleContactDao.findByIdAndOrg(contact.id, contact.organizationId + 1).block()
        then:
            loaded == null
    }
    
    def "should update a simple contact"() {
        
        given:
            def assigner = userTestHelper.newUser()
            def contact = clientTestHelper.newSimpleContact(assigner)
            def curator = userTestHelper.newUser("Curator2", assigner.organization)
            def timeAfterCreation = Instant.now()
        when:
            def loaded = simpleContactDao.findById(contact.id).block()
            def prevModified = loaded.modified
        then:
            loaded != null
            loaded.id == contact.id
            loaded.curatorIds.length == 0
            loaded.organizationId == contact.id
            loaded.birthday == null
            loaded.firstName == contact.firstName
            loaded.secondName == loaded.secondName
            loaded.thirdName == null
            loaded.phoneNumbers.length == 0
            loaded.emails.length == 0
            loaded.sites.length == 0
            loaded.messengers.length == 0
            loaded.company == null
            loaded.created.isBefore(timeAfterCreation)
            loaded.version == 0
        when:
            Thread.sleep(1000)
            loaded.company = "New Company"
            loaded.messengers = [new DefaultContactMessenger("test", MessengerType.SKYPE)]
            loaded.sites = [new DefaultContactSite("google.com", SiteType.HOME)]
            loaded.emails = [new DefaultContactEmail("test@test.com", EmailType.WORK)]
            loaded.phoneNumbers = [new DefaultContactPhoneNumber("+375", "25", "124124", PhoneNumberType.WORK)]
            loaded.thirdName = "Third name"
            loaded.birthday = LocalDate.of(1900, 04, 11)
            loaded.curatorIds = [curator.id]
            simpleContactDao.update(loaded).block()
            def reloaded = simpleContactDao.findById(loaded.id).block()
        then:
            reloaded.id == loaded.id
            reloaded.curatorIds == loaded.curatorIds
            reloaded.company == loaded.company
            reloaded.messengers == loaded.messengers
            reloaded.sites == loaded.sites
            reloaded.emails == loaded.emails
            reloaded.phoneNumbers == loaded.phoneNumbers
            reloaded.birthday == loaded.birthday
            reloaded.modified.isAfter(reloaded.created)
            reloaded.modified.isAfter(prevModified)
            reloaded.version == 1
        when:
            reloaded.thirdName = "Third name 2"
            simpleContactDao.update(loaded).block()
            reloaded = simpleContactDao.findById(loaded.id).block()
        then:
            reloaded.version == 2
    }
    
    def "should failed updating a simple contact by outdated version reason"() {
        
        given:
            def assigner = userTestHelper.newUser()
            def contact = clientTestHelper.newSimpleContact(assigner)
        when:
            def loaded = simpleContactDao.findById(contact.id).block()
        then:
            loaded != null
            loaded.id == contact.id
            loaded.version == 0
        when:
            loaded.setVersion(5)
            simpleContactDao.update(loaded).block()
        then:
            thrown NotActualObjectDaoException
    }
    
    def "should load a page of contacts"() {
        
        given:
    
            def firstOrgContactsCount = 20
            def secondOrgContactsCount = 5
    
            def firstOrg = userTestHelper.newOrg()
            def firstUser = userTestHelper.newUser("User1", firstOrg)
            def secondOrg = userTestHelper.newOrg()
            def secondUser = userTestHelper.newUser("User2", secondOrg)
    
            firstOrgContactsCount.times {
                clientTestHelper.newSimpleContact(firstUser)
            }
    
            secondOrgContactsCount.times {
                clientTestHelper.newSimpleContact(secondUser)
            }
        
            List<SimpleContact> loadedContacts = []
    
        when:
            def page = simpleContactDao.findPageByOrg(0, 5, firstOrg.id).block()
            loadedContacts.addAll(page.entities)
        then:
            page != null
            page.totalSize == firstOrgContactsCount
            page.entities.size() == 5
        when:
            page = simpleContactDao.findPageByOrg(17, 5, firstOrg.id).block()
        then:
            page != null
            page.totalSize == firstOrgContactsCount
            page.entities.size() == 3
            !page.entities.stream().anyMatch({ loadedContacts.contains(it) })
    }
}
