package crm.integration.test.web

import crm.integration.test.DefaultSpecification
import crm.integration.test.web.config.WebTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

import java.time.Duration

@WebFluxTest(properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(classes = WebTestConfig)
class WebSpecification extends DefaultSpecification {
 
  @Autowired
  protected WebTestClient webClient
  
  @Override
  def setup() {
    webClient = webClient
        .mutate()
        .responseTimeout(Duration.ofMinutes(5))
        .build()
  }
}
