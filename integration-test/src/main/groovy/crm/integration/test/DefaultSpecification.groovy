package crm.integration.test

import crm.integration.test.config.BaseTestConfig
import crm.integration.test.util.TestUtils
import org.jetbrains.annotations.NotNull
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

@ContextConfiguration(classes = BaseTestConfig)
class DefaultSpecification extends Specification {
  
  String prefix = getClass().getSimpleName()
  
  def setupSpec() {
  }
  
  def setup() {
  }
  
  def cleanup() {
  }
  
  def cleanupSpec() {
  }
  
  protected <T> T waitForResult(@NotNull Mono<T> mono) {
    return TestUtils.waitForResult(mono)
  }
  
  protected <T> List<T> waitForResults(@NotNull Flux<T> flux) {
    return TestUtils.waitForResults(flux)
  }
}
