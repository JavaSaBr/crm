package com.ss.jcrm.security.test

import com.ss.jcrm.integration.test.DefaultSpecification
import com.ss.jcrm.security.Passwords

class PasswordsTest extends DefaultSpecification {

    def "should generate a new random password"() {

        given:
            def length = 26
        when:
            def password = Passwords.nextPassword(length)
        then:
            password != null
            password.length() == length
    }

    def "should generate a new salt"() {

        when:
            def last = Passwords.nextSalt
        then:
            last != null
            last.length > 0
    }

    def "should create a string based password's hash"() {

        given:
            def password = Passwords.nextPassword(30)
            def salt = Passwords.nextSalt
        when:
            def hash = Passwords.hash(password, salt)
        then:
            hash != null
            hash.length > 0
    }

    def "should create a char array based password's hash"() {

        given:
            def password = Passwords.nextPassword(30)
            def salt = Passwords.nextSalt
        when:
            def hash = Passwords.hash(password.toCharArray(), salt)
        then:
            hash != null
            hash.length > 0
    }

    def "should validate a string based correct password"() {

        given:
            def password = Passwords.nextPassword(30)
            def salt = Passwords.nextSalt
            def hash = Passwords.hash(password, salt)
        when:
            def correct = Passwords.isCorrect(password, salt, hash)
            def incorrect = Passwords.isCorrect(password + "1", salt, hash)
        then:
            correct
            !incorrect
    }

    def "should validate a char array based correct password"() {

        given:
            def password = Passwords.nextPassword(30)
            def salt = Passwords.nextSalt
            def hash = Passwords.hash(password, salt)
        when:
            def correct = Passwords.isCorrect(password, salt, hash)
            def incorrect = Passwords.isCorrect(password + "1", salt, hash)
        then:
            correct
            !incorrect
    }
}
