package crm.security

import crm.security.service.PasswordService
import org.springframework.beans.factory.annotation.Autowired

class DefaultPasswordServiceTest extends SecuritySpecification {
  
  @Autowired
  PasswordService passwordService
  
  def "should generate new random password"() {
    given:
        def length = 26
    when:
        def password = passwordService.nextPassword(length)
    then:
        password != null
        password.length() == length
  }
  
  def "should generate new salt"() {
    when:
        def last = passwordService.nextSalt
    then:
        last != null
        last.length > 0
  }
  
  def "should create string based password's hash"() {
    given:
        def password = passwordService.nextPassword(30)
        def salt = passwordService.nextSalt
    when:
        def hash = passwordService.hash(password, salt)
    then:
        hash != null
        hash.length > 0
  }
  
  def "should create char array based password's hash"() {
    given:
        def password = passwordService.nextCharPassword(30)
        def salt = passwordService.nextSalt
    when:
        def hash = passwordService.hash(password, salt)
    then:
        hash != null
        hash.length > 0
  }
  
  def "should validate string based correct password"() {
    given:
        def password = passwordService.nextPassword(30)
        def salt = passwordService.nextSalt
        def hash = passwordService.hash(password, salt)
    when:
        def correct = passwordService.isCorrect(password, salt, hash)
        def incorrect = passwordService.isCorrect(password + "1", salt, hash)
    then:
        correct
        !incorrect
  }
  
  def "should validate char array based correct password"() {
    given:
        def password = passwordService.nextPassword(30)
        def salt = passwordService.nextSalt
        def hash = passwordService.hash(password, salt)
    when:
        def correct = passwordService.isCorrect(password, salt, hash)
        def incorrect = passwordService.isCorrect(password + "1", salt, hash)
    then:
        correct
        !incorrect
  }
}
