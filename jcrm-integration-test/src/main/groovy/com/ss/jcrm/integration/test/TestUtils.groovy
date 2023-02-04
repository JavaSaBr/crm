package com.ss.jcrm.integration.test

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class TestUtils {
  static <T> T waitForResult(Mono<T> mono) {
    return mono.block();
  }
  
  static <T> List<T> waitForResults(Flux<T> flux) {
    return flux.collectList().block();
  }
}
