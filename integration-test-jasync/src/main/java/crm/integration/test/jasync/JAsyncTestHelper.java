package crm.integration.test.jasync;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class JAsyncTestHelper {

  ConnectionPool<? extends ConcreteConnection> connectionPool;
  String schema;

  AtomicInteger idGenerator = new AtomicInteger(0);

  protected String nextId(String prefix, String postfix) {
    return "%s%d%s".formatted(prefix, idGenerator.incrementAndGet(), postfix);
  }
}
