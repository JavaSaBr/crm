package com.ss.jcrm.user.jdbc.test.dao


import com.ss.jcrm.user.api.dao.EmailConfirmationDao
import com.ss.jcrm.user.jdbc.test.JdbcUserSpecification
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

class JdbcEmailConfirmationDaoTest extends JdbcUserSpecification {

    @Autowired
    EmailConfirmationDao emailConfirmationDao

    def "should a new email confirmation"(String code, String email) {

        when:
            def confirmation = emailConfirmationDao.create(code, email, Instant.now())
        then:
            confirmation.id != 0
            confirmation.email == email
            confirmation.code == code
        where:
            code << ["234", "565434", "6775"]
            email << ["test1@email.com", "test2@email.com", "test3@eamil.com"]
    }

    def "should a new email confirmation async"(String code, String email) {

        when:
            def confirmation = emailConfirmationDao.createAsync(code, email, Instant.now()).join()
        then:
            confirmation.id != 0
            confirmation.email == email
            confirmation.code == code
        where:
            code << ["234", "565434", "6775"]
            email << ["test1@email.com", "test2@email.com", "test3@eamil.com"]
    }

    def "should create and load a new email confirmation by id"() {

        given:
            def code = "3453446"
            def email = "test5@email.com"
            def expiration = Instant.now().plusSeconds(60)
            def created = emailConfirmationDao.create(code, email, expiration)
        when:
            def loaded = emailConfirmationDao.findById(created.id)
        then:
            loaded != null
            loaded.getId() != 0L
            loaded.code == code
            loaded.email == email
            loaded.expiration == expiration
    }

    def "should create and load a new email confirmation by code and email"() {

        given:
            def code = "12312"
            def email = "test6@email.com"
            def expiration = Instant.now().plusSeconds(60)
            emailConfirmationDao.create(code, email, expiration)
        when:
            def loaded = emailConfirmationDao.findByEmailAndCode(email, code)
        then:
            loaded != null
            loaded.getId() != 0L
            loaded.code == code
            loaded.email == email
            loaded.expiration == expiration
    }

    def "should delete created email confirmation by id"() {

        given:
            def code = "6242"
            def email = "test6@email.com"
            def expiration = Instant.now().plusSeconds(60)
            emailConfirmationDao.create(code, email, expiration)
        when:
            def loaded = emailConfirmationDao.findByEmailAndCode(email, code)
        then:
            loaded != null
            loaded.getId() != 0L
            loaded.code == code
            loaded.email == email
            loaded.expiration == expiration
        when:
            emailConfirmationDao.delete(loaded.id)
            def loaded2 = emailConfirmationDao.findById(loaded.id)
        then:
            loaded2 == null
    }
}
