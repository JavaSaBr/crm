package com.ss.jcrm.integration.test.web

import com.ss.jcrm.integration.test.DefaultSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@WebAppConfiguration
@ContextConfiguration(classes = WebSpecificationConfig)
class WebSpecification extends DefaultSpecification {

    @Autowired
    WebApplicationContext wac

    MockMvc mvc

    def setup() {
        mvc = webAppContextSetup(wac).build()
    }
}
