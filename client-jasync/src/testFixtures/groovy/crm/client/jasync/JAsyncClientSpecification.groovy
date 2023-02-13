package crm.client.jasync

import com.ss.jcrm.integration.test.DefaultSpecification
import crm.client.api.ClientDbTestHelper
import crm.client.jasync.config.JAsyncClientTestConfig
import crm.user.api.UserDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JAsyncClientTestConfig)
class JAsyncClientSpecification extends DefaultSpecification {
  
  @Autowired
  UserDbTestHelper userTestHelper
  
  @Autowired
  ClientDbTestHelper clientTestHelper
}
