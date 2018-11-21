package com.ss.jcrm.integration.test.web

import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@Configuration
@WebAppConfiguration
class WebSpecificationConfig {

    @Autowired
    WebApplicationContext wac

    @Bean
    @NotNull MockMvc mockMvc() {
        return webAppContextSetup(wac).build()
    }
}
