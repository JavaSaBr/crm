package crm.security.web

import crm.integration.test.web.WebSpecification
import crm.security.web.service.UnsafeTokenService
import crm.user.api.UserDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = WebSecurityTestConfig)
class WebSecuritySpecification extends WebSpecification {
  
  @Autowired
  UserDbTestHelper userTestHelper
  
  @Autowired
  UnsafeTokenService unsafeTokenService
  
  def setup() {}
}
