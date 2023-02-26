package crm.dictionary.web

import crm.base.util.Reloadable
import crm.dictionary.web.config.DictionaryWebTestConfig
import crm.integration.test.web.WebSpecification
import crm.base.web.config.ApiEndpoint
import crm.dictionary.api.DictionaryDbTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = DictionaryWebTestConfig)
class DictionarySpecification extends WebSpecification {
  
  @Autowired
  ApiEndpoint dictionaryApiEndpoint
  
  @Autowired
  DictionaryDbTestHelper dictionaryTestHelper
  
  @Autowired
  List<Reloadable> reloadableServices
  
  def contextPath
  
  def setup() {
    
    reloadableServices.each {
      it.reload()
    }
    
    contextPath = dictionaryApiEndpoint.contextPath()
  }
}
