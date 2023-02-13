package crm.dictionary.jasync

import com.ss.jcrm.integration.test.DefaultSpecification
import crm.dictionary.api.DictionaryDbTestHelper
import crm.dictionary.jasync.config.JAsyncDictionaryTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = JAsyncDictionaryTestConfig)
class JAsyncDictionarySpecification extends DefaultSpecification {
  
  @Autowired
  DictionaryDbTestHelper dictionaryTestHelper
}
