package com.ss.jcrm.registration.web.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import crm.user.api.test.UserTestHelper
import com.ss.jcrm.user.contact.api.MessengerType
import com.ss.jcrm.user.contact.api.PhoneNumberType
import com.ss.jcrm.user.contact.api.resource.MessengerResource
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource
import com.ss.jcrm.web.config.ApiEndpointServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import java.util.concurrent.ThreadLocalRandom

@ContextConfiguration(classes = RegistrationSpecificationConfig)
class RegistrationSpecification extends WebSpecification {

    @Autowired
    ApiEndpointServer registrationApiEndpointServer

    @Autowired
    UserTestHelper userTestHelper

    @Autowired
    DictionaryTestHelper dictionaryTestHelper
    
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
