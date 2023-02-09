package com.ss.jcrm.integration.test

import com.ss.jcrm.integration.test.config.BaseTestConfig
import org.jetbrains.annotations.NotNull
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

@ContextConfiguration(classes = BaseTestConfig)
class DefaultSpecification extends Specification {
  
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
