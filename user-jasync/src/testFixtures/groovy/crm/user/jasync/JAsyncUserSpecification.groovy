package crm.user.jasync

import com.ss.jcrm.dictionary.api.test.DictionaryDbTestHelper
import com.ss.jcrm.integration.test.DefaultSpecification
import crm.user.api.UserDbTestHelper
import crm.user.jasync.config.JAsyncUserTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JAsyncUserTestConfig)
class JAsyncUserSpecification extends DefaultSpecification {
  
  @Autowired
  DictionaryDbTestHelper dictionaryTestHelper
  
  @Autowired
  UserDbTestHelper userTestHelper
  
  def setup() {
    //userTestHelper.clearAllData()
    //dictionaryTestHelper.clearAllData()
  }
}
