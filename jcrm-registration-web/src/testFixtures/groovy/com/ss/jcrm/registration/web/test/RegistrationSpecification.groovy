package com.ss.jcrm.registration.web.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.ss.jcrm.integration.test.web.WebSpecification

import crm.base.web.config.ApiEndpoint
import crm.contact.api.MessengerType
import crm.contact.api.PhoneNumberType
import crm.contact.api.resource.MessengerResource
import crm.contact.api.resource.PhoneNumberResource
import crm.dictionary.api.DictionaryDbTestHelper
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
        contextPath = registrationApiEndpointServer.contextPath()
    }
    
    List<MessengerResource> generateMessengers() {
        return [
            new MessengerResource("telegaTest2", MessengerType.TELEGRAM.id()),
            new MessengerResource("skypeTest1", MessengerType.SKYPE.id())
        ]
    }
    
    List<PhoneNumberResource> generatePhoneNumbers() {
        
        def current = ThreadLocalRandom.current()
        
        return [
            new PhoneNumberResource(
                "+${current.nextInt(1, 300)}",
                "${current.nextInt(20, 200)}",
                "${current.nextInt(20000, 90000)}",
                PhoneNumberType.UNKNOWN.id()),
            new PhoneNumberResource(
                "+${current.nextInt(1, 300)}",
                "${current.nextInt(20, 200)}",
                "${current.nextInt(20000, 90000)}",
                PhoneNumberType.UNKNOWN.id())
        ]
    }
}
