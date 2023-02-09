package com.ss.jcrm.registration.web.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.ss.jcrm.dictionary.api.test.DictionaryDbTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification

import com.ss.jcrm.user.contact.api.MessengerType
import com.ss.jcrm.user.contact.api.PhoneNumberType
import com.ss.jcrm.user.contact.api.resource.MessengerResource
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource
import crm.base.web.config.ApiEndpoint
import crm.user.api.UserDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import java.util.concurrent.ThreadLocalRandom

@ContextConfiguration(classes = RegistrationSpecificationConfig)
class RegistrationSpecification extends WebSpecification {

    @Autowired
    ApiEndpoint registrationApiEndpointServer

    @Autowired
    UserDbTestHelper userTestHelper

    @Autowired
    DictionaryDbTestHelper dictionaryTestHelper
    
    @Autowired
    ObjectMapper objectMapper

    def contextPath

    def setup() {

        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()

        contextPath = registrationApiEndpointServer.contextPath()
    }
    
    def generateMessengers() {
        new MessengerResource[]{
            new MessengerResource("telegaTest2", (int) MessengerType.TELEGRAM.id),
            new MessengerResource("skypeTest1", (int) MessengerType.SKYPE.id)
        }
    }
    
    def generatePhoneNumbers() {
        
        def current = ThreadLocalRandom.current()
        
        new PhoneNumberResource[]{
            new PhoneNumberResource(
                "+${current.nextInt(1, 300)}",
                "${current.nextInt(20, 200)}",
                "${current.nextInt(20000, 90000)}",
                (int) PhoneNumberType.UNKNOWN.id
            ),
            new PhoneNumberResource(
                "+${current.nextInt(1, 300)}",
                "${current.nextInt(20, 200)}",
                "${current.nextInt(20000, 90000)}",
                (int) PhoneNumberType.UNKNOWN.id
            )
        }
    }
}
