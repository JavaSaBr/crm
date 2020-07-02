package com.ss.jcrm.registration.web.test

import com.ss.jcrm.dictionary.api.test.DictionaryTestHelper
import com.ss.jcrm.integration.test.web.WebSpecification
import com.ss.jcrm.user.api.test.UserTestHelper
import com.ss.jcrm.user.contact.api.MessengerType
import com.ss.jcrm.user.contact.api.PhoneNumberType
import com.ss.jcrm.user.contact.api.resource.MessengerResource
import com.ss.jcrm.user.contact.api.resource.PhoneNumberResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import java.util.concurrent.ThreadLocalRandom

@ContextConfiguration(classes = RegistrationSpecificationConfig)
class RegistrationSpecification extends WebSpecification {

    @Autowired
    UserTestHelper userTestHelper

    @Autowired
    DictionaryTestHelper dictionaryTestHelper

    def setup() {
        userTestHelper.clearAllData()
        dictionaryTestHelper.clearAllData()
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
