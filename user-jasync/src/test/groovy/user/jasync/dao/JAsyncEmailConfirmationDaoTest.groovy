package user.jasync.dao

import crm.user.api.dao.EmailConfirmationDao
import crm.user.jasync.JAsyncUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

class JAsyncEmailConfirmationDaoTest extends JAsyncUserSpecification {
  
  @Autowired
  EmailConfirmationDao emailConfirmationDao
  
  def "should new email confirmation"(String code, String email) {
    when:
        def confirmation = emailConfirmationDao.create(code, email, Instant.now()).block()
    then:
        confirmation.id() != 0
        confirmation.email() == email
        confirmation.code() == code
    where:
        code << ["234", "565434", "6775"]
        email << ["test1@email.com", "test2@email.com", "test3@eamil.com"]
  }
  
  def "should create and load new email confirmation by id"() {
    given:
        def code = "3453446"
        def email = userTestHelper.nextEmail()
        def expiration = Instant.now().plusSeconds(60)
        def created = emailConfirmationDao.create(code, email, expiration).block()
    when:
        def loaded = emailConfirmationDao.findById(created.id()).block()
    then:
        loaded != null
        loaded.id() != 0L
        loaded.code() == code
        loaded.email() == email
        loaded.expiration().toEpochMilli() == expiration.toEpochMilli()
  }
  
  def "should create and load new email confirmation by code and email"() {
    given:
        def code = "12312"
        def email = "test6@email.com"
        def expiration = Instant.now().plusSeconds(60)
        emailConfirmationDao.create(code, email, expiration).block()
    when:
        def loaded = emailConfirmationDao.findByEmailAndCode(email, code).block()
    then:
        loaded != null
        loaded.id() != 0L
        loaded.code() == code
        loaded.email() == email
        loaded.expiration().toEpochMilli() == expiration.toEpochMilli()
  }
  
  def "should delete created email confirmation by id"() {
    given:
        def code = "6242"
        def email = "test6@email.com"
        def expiration = Instant.now().plusSeconds(60)
        emailConfirmationDao.create(code, email, expiration).block()
    when:
        def loaded = emailConfirmationDao.findByEmailAndCode(email, code).block()
    then:
        loaded != null
        loaded.id() != 0L
        loaded.code() == code
        loaded.email() == email
        loaded.expiration().toEpochMilli() == expiration.toEpochMilli()
    when:
        emailConfirmationDao.deleteById(loaded.id).block()
        def loaded2 = emailConfirmationDao.findById(loaded.id).block()
    then:
        loaded2 == null
  }
}
