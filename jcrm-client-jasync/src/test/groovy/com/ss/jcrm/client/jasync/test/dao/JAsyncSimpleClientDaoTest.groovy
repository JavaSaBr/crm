package com.ss.jcrm.client.jasync.test.dao

import com.ss.jcrm.client.api.*
import com.ss.jcrm.client.api.dao.SimpleClientDao
import com.ss.jcrm.client.api.impl.DefaultClientEmail
import com.ss.jcrm.client.api.impl.DefaultClientMessenger
import com.ss.jcrm.client.api.impl.DefaultClientPhoneNumber
import com.ss.jcrm.client.api.impl.DefaultContactSite
import com.ss.jcrm.client.jasync.test.JAsyncClientSpecification
import com.ss.jcrm.dao.exception.NotActualObjectDaoException
import com.ss.jcrm.jasync.util.JAsyncUtils
import com.ss.jcrm.user.api.User
import com.ss.rlib.common.util.array.Array
import com.ss.rlib.common.util.array.ArrayFactory
import org.springframework.beans.factory.annotation.Autowired

import java.time.Clock
import java.time.Instant
import java.time.LocalDate

class JAsyncSimpleClientDaoTest extends JAsyncClientSpecification {
    
    @Autowired
    SimpleClientDao simpleClientDao
    
    def "should create and load simple client"() {
        
        given:
            
            def assigner = userTestHelper.newUser("Test1")
            def currentTime = Instant.now()
        
            Array<User> curators = ArrayFactory.asArray(
                userTestHelper.newUser("Curator1"),
                userTestHelper.newUser("Curator2")
            )
            ClientPhoneNumber[] phoneNumbers = [
                new DefaultClientPhoneNumber("+7", "123", "456789", PhoneNumberType.WORK),
                new DefaultClientPhoneNumber("+7", "123", "098765", PhoneNumberType.MOBILE)
            ]
            ClientEmail[] emails = [
                new DefaultClientEmail("test@test.com", EmailType.WORK),
                new DefaultClientEmail("test2@test2.com", EmailType.HOME),
            ]
            ClientSite[] sites = [
                new DefaultContactSite("google.com", SiteType.HOME),
            ]
            ClientMessenger[] messengers = [
                new DefaultClientMessenger("user1", MessengerType.SKYPE),
                new DefaultClientMessenger("user2", MessengerType.TELEGRAM),
            ]
            def birthday = LocalDate.of(1900, 3, 12)
        when:
            def client = simpleClientDao.create(
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
            client != null
            client.id > 0
            client.assignerId == assigner.id
            client.curatorIds == JAsyncUtils.toIds(curators)
            client.organizationId == assigner.organization.id
            client.firstName == "First name"
            client.secondName == "Second name"
            client.thirdName == "Third name"
            client.birthday == birthday
            client.phoneNumbers == phoneNumbers
            client.emails == emails
            client.sites == sites
            client.messengers == messengers
            client.company == "My company"
            client.created.isAfter(currentTime)
            client.modified.isAfter(currentTime)
        when:
            client = simpleClientDao.findById(client.id).block()
        then:
            client != null
            client.id > 0
            client.assignerId == assigner.id
            client.curatorIds == JAsyncUtils.toIds(curators)
            client.organizationId == assigner.organization.id
            client.firstName == "First name"
            client.secondName == "Second name"
            client.thirdName == "Third name"
            client.birthday == birthday
            client.phoneNumbers == phoneNumbers
            client.emails == emails
            client.sites == sites
            client.messengers == messengers
            client.company == "My company"
            client.created.isAfter(currentTime)
            client.modified.isAfter(currentTime)
    }
    
    def "should load simple client"() {
        
        given:
            def client = clientTestHelper.newSimpleClient()
        when:
            def loaded = simpleClientDao.findById(client.id).block()
        then:
            loaded != null
            loaded.id == client.id
            loaded.organizationId == client.id
            loaded.firstName == client.firstName
            loaded.secondName == loaded.secondName
    }
    
    def "should load simple client only under its organization"() {
        
        given:
            def client = clientTestHelper.newSimpleClient()
        when:
            def loaded = simpleClientDao.findByIdAndOrg(client.id, client.organizationId).block()
        then:
            loaded != null
            loaded.id == client.id
            loaded.organizationId == client.id
            loaded.firstName == client.firstName
            loaded.secondName == loaded.secondName
        when:
            loaded = simpleClientDao.findByIdAndOrg(client.id, client.organizationId + 1).block()
        then:
            loaded == null
    }
    
    def "should update simple client correctly"() {
        
        given:
            def assigner = userTestHelper.newUser()
            def client = clientTestHelper.newSimpleClient(assigner)
            def curator = userTestHelper.newUser("Curator2", assigner.organization)
            def timeAfterCreation = Instant.now(Clock.systemUTC())
        when:
            def loaded = simpleClientDao.findById(client.id).block()
            def prevModified = loaded.modified
        then:
            loaded != null
            loaded.id == client.id
            loaded.curatorIds.length == 0
            loaded.organizationId == client.id
            loaded.birthday == null
            loaded.firstName == client.firstName
            loaded.secondName == loaded.secondName
            loaded.thirdName == loaded.thirdName
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
            loaded.messengers = [new DefaultClientMessenger("test", MessengerType.SKYPE)]
            loaded.sites = [new DefaultContactSite("google.com", SiteType.HOME)]
            loaded.emails = [new DefaultClientEmail("test@test.com", EmailType.WORK)]
            loaded.phoneNumbers = [new DefaultClientPhoneNumber("+375", "25", "124124", PhoneNumberType.WORK)]
            loaded.thirdName = "Third name 2"
            loaded.birthday = LocalDate.of(1900, 04, 11)
            loaded.curatorIds = [curator.id]
            simpleClientDao.update(loaded).block()
            def reloaded = simpleClientDao.findById(loaded.id).block()
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
            simpleClientDao.update(reloaded).block()
            reloaded = simpleClientDao.findById(reloaded.id).block()
        then:
            reloaded.version == 2
    }
    
    def "should failed updating simple client by outdated version reason"() {
        
        given:
            def assigner = userTestHelper.newUser()
            def client = clientTestHelper.newSimpleClient(assigner)
        when:
            def loaded = simpleClientDao.findById(client.id).block()
        then:
            loaded != null
            loaded.id == client.id
            loaded.version == 0
        when:
            loaded.setVersion(5)
            simpleClientDao.update(loaded).block()
        then:
            thrown NotActualObjectDaoException
    }
    
    def "should load page of clients"() {
        
        given:
    
            def firstOrgClientsCount = 20
            def secondOrgClientsCount = 5
    
            def firstOrg = userTestHelper.newOrg()
            def firstUser = userTestHelper.newUser("User1", firstOrg)
            def secondOrg = userTestHelper.newOrg()
            def secondUser = userTestHelper.newUser("User2", secondOrg)
    
            firstOrgClientsCount.times {
                clientTestHelper.newSimpleClient(firstUser)
            }
    
            secondOrgClientsCount.times {
                clientTestHelper.newSimpleClient(secondUser)
            }
        
            List<SimpleClient> loadedClients = []
    
        when:
            def page = simpleClientDao.findPageByOrg(0, 5, firstOrg.id).block()
            loadedClients.addAll(page.entities)
        then:
            page != null
            page.totalSize == firstOrgClientsCount
            page.entities.size() == 5
        when:
            page = simpleClientDao.findPageByOrg(17, 5, firstOrg.id).block()
        then:
            page != null
            page.totalSize == firstOrgClientsCount
            page.entities.size() == 3
            !page.entities.stream().anyMatch({ loadedClients.contains(it) })
    }
}
