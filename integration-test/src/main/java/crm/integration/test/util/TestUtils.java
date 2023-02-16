package crm.integration.test.util;

import java.util.List;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@UtilityClass
public class TestUtils {

  public static <T> T waitForResult(Mono<T> mono) {
    return mono.block();
  }

  public static <T> List<T> waitForResults(Flux<T> flux) {
    return flux.collectList().block();
  }
}
