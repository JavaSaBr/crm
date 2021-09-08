package com.ss.jcrm.integration.test.web

import com.ss.jcrm.integration.test.DefaultSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(classes = WebSpecificationConfig)
class WebSpecification extends DefaultSpecification {
    
    @Autowired
    protected WebTestClient webClient
    
}
