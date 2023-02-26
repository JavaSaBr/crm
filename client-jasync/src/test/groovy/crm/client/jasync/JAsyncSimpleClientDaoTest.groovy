package crm.client.jasync

import crm.client.api.dao.SimpleClientDao

import crm.dao.exception.NotActualObjectDaoException

import crm.client.api.SimpleClient
import crm.contact.api.Email
import crm.contact.api.EmailType
import crm.contact.api.Messenger
import crm.contact.api.MessengerType
import crm.contact.api.PhoneNumber
import crm.contact.api.PhoneNumberType
import crm.contact.api.Site
import crm.contact.api.SiteType
import crm.jasync.util.JAsyncUtils
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant
import java.time.LocalDate

class JAsyncSimpleClientDaoTest extends JAsyncClientSpecification {
  
  @Autowired
  SimpleClientDao simpleClientDao
  
  def "should create and load simple client"() {
    given:
        
        def assigner = userTestHelper.newUser()
        def currentTime = Instant.now()
        
        def curators = Set.of(
            userTestHelper.newUser(),
            userTestHelper.newUser())
        
        def phoneNumbers = Set.of(
            PhoneNumber.of("+7", "123", "456789", PhoneNumberType.WORK),
            PhoneNumber.of("+7", "123", "098765", PhoneNumberType.MOBILE))
        
        def emails = Set.of(
            Email.of("test@test.com", EmailType.WORK),
            Email.of("test2@test2.com", EmailType.HOME))
        
        def sites = Set.of(Site.of("google.com", SiteType.HOME))
        def messengers = Set.of(
            Messenger.of("user1", MessengerType.SKYPE),
            Messenger.of("user2", MessengerType.TELEGRAM))
        
        def birthday = LocalDate.of(1900, 3, 12)
    when:
        def client = simpleClientDao.create(
            assigner,
            curators,
            assigner.organization(),
            "First name",
            "Second name",
            "Third name",
            birthday,
            phoneNumbers,
            emails,
            sites,
            messengers,
            "My company").block()
    then:
        client != null
        client.id() > 0
        client.assignerId() == assigner.id()
        client.curatorIds() == JAsyncUtils.toIds(curators)
        client.organizationId() == assigner.organization().id()
        client.firstName() == "First name"
        client.secondName() == "Second name"
        client.thirdName() == "Third name"
        client.birthday() == birthday
        client.phoneNumbers() == phoneNumbers
        client.emails() == emails
        client.sites() == sites
        client.messengers() == messengers
        client.company() == "My company"
        client.created().isAfter(currentTime)
        client.modified().isAfter(currentTime)
    when:
        client = simpleClientDao.findById(client.id()).block()
    then:
        client != null
        client.id() > 0
        client.assignerId() == assigner.id()
        client.curatorIds() == JAsyncUtils.toIds(curators)
        client.organizationId() == assigner.organization().id()
        client.firstName() == "First name"
        client.secondName() == "Second name"
        client.thirdName() == "Third name"
        client.birthday() == birthday
        client.phoneNumbers() == phoneNumbers
        client.emails() == emails
        client.sites() == sites
        client.messengers() == messengers
        client.company() == "My company"
        client.created().isAfter(currentTime)
        client.modified().isAfter(currentTime)
  }
  
  def "should load simple client"() {
    
    given:
        def client = clientTestHelper.newSimpleClient()
    when:
        def loaded = simpleClientDao.findById(client.id()).block()
    then:
        loaded != null
        loaded.id() == client.id()
        loaded.organizationId() == client.organizationId()
        loaded.firstName() == client.firstName()
        loaded.secondName() == loaded.secondName()
  }
  
  def "should load simple client only under its organization"() {
    
    given:
        def client = clientTestHelper.newSimpleClient()
    when:
        def loaded = simpleClientDao.findByIdAndOrganization(client.id(), client.organizationId()).block()
    then:
        loaded != null
        loaded.id() == client.id()
        loaded.organizationId() == client.organizationId()
        loaded.firstName() == client.firstName()
        loaded.secondName() == loaded.secondName()
    when:
        loaded = simpleClientDao.findByIdAndOrganization(client.id(), client.organizationId() + 1).block()
    then:
        loaded == null
  }
  
  def "should update simple client correctly"() {
    given:
        def assigner = userTestHelper.newUser()
        def client = clientTestHelper.newSimpleClient(assigner)
        def curator = userTestHelper.newUser(assigner.organization())
        def timeAfterCreation = Instant.now()
    when:
        def loaded = simpleClientDao.findById(client.id()).block()
        def prevModified = loaded.modified()
    then:
        loaded != null
        loaded.id() == client.id()
        loaded.curatorIds().length == 0
        loaded.organizationId() == client.organizationId()
        loaded.birthday() == null
        loaded.firstName() == client.firstName()
        loaded.secondName() == loaded.secondName()
        loaded.thirdName() == loaded.thirdName()
        loaded.phoneNumbers().size() == 0
        loaded.emails().size() == 0
        loaded.sites().size() == 0
        loaded.messengers().size() == 0
        loaded.company() == null
        loaded.created().isBefore(timeAfterCreation)
        loaded.version() == 0
    when:
        Thread.sleep(1000)
        loaded.company("New Company")
        loaded.messengers(Set.of(Messenger.of("test", MessengerType.SKYPE)))
        loaded.sites(Set.of(Site.of("google.com", SiteType.HOME)))
        loaded.emails(Set.of(Email.of("test@test.com", EmailType.WORK)))
        loaded.phoneNumbers(Set.of(PhoneNumber.of("+375", "25", "124124", PhoneNumberType.WORK)))
        loaded.thirdName("Third name 2")
        loaded.birthday(LocalDate.of(1900, 04, 11))
        loaded.curatorIds(new long[]{curator.id()})
        simpleClientDao.update(loaded).block()
        def reloaded = simpleClientDao.findById(loaded.id()).block()
    then:
        reloaded.id() == loaded.id()
        reloaded.curatorIds() == loaded.curatorIds()
        reloaded.company() == loaded.company()
        reloaded.messengers() == loaded.messengers()
        reloaded.sites() == loaded.sites()
        reloaded.emails() == loaded.emails()
        reloaded.phoneNumbers() == loaded.phoneNumbers()
        reloaded.birthday() == loaded.birthday()
        reloaded.modified().isAfter(reloaded.created())
        reloaded.modified().isAfter(prevModified)
        reloaded.version() == 1
    when:
        reloaded.thirdName("Third name 2")
        simpleClientDao.update(reloaded).block()
        reloaded = simpleClientDao.findById(reloaded.id()).block()
    then:
        reloaded.version() == 2
  }
  
  def "should failed updating simple client by outdated version reason"() {
    given:
        def assigner = userTestHelper.newUser()
        def client = clientTestHelper.newSimpleClient(assigner)
    when:
        def loaded = simpleClientDao.findById(client.id()).block()
    then:
        loaded != null
        loaded.id() == client.id()
        loaded.version() == 0
    when:
        loaded.version(5)
        simpleClientDao.update(loaded).block()
    then:
        thrown NotActualObjectDaoException
  }
  
  def "should load page of clients"() {
    
    given:
        
        def firstOrgClientsCount = 20
        def secondOrgClientsCount = 5
        
        def firstOrganization = userTestHelper.newOrganization()
        def firstUser = userTestHelper.newUser(firstOrganization)
        def secondOrganization = userTestHelper.newOrganization()
        def secondUser = userTestHelper.newUser(secondOrganization)
        
        firstOrgClientsCount.times {
          clientTestHelper.newSimpleClient(firstUser)
        }
        
        secondOrgClientsCount.times {
          clientTestHelper.newSimpleClient(secondUser)
        }
        
        List<SimpleClient> loadedClients = []
    
    when:
        def page = simpleClientDao.findPageByOrg(0, 5, firstOrganization.id()).block()
        loadedClients.addAll(page.entities())
    then:
        page != null
        page.totalSize() == firstOrgClientsCount
        page.entities().size() == 5
    when:
        page = simpleClientDao.findPageByOrg(17, 5, firstOrganization.id()).block()
    then:
        page != null
        page.totalSize() == firstOrgClientsCount
        page.entities().size() == 3
        !page.entities().stream().anyMatch({ loadedClients.contains(it) })
  }
}
