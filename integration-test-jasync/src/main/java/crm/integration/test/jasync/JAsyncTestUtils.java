package crm.integration.test.jasync;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import crm.integration.test.db.config.DbTestConfig;
import crm.jasync.util.JAsyncUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class JAsyncTestUtils {

  public static void clearTable(
      ConnectionPool<? extends ConcreteConnection> connectionPool,
      String schema,
      String... tableNames) {
    for (String tableName : tableNames) {
      executeQuery(connectionPool, "delete from \"%s\".\"%s\"".formatted(schema, tableName));
    }
  }

  public static void executeQuery(ConnectionPool<? extends ConcreteConnection> connectionPool, String query) {
    connectionPool
        .sendQuery(query)
        .join();
  }

  public static ConnectionPool<PostgreSQLConnection> newConnectionPool(PostgreSQLContainer container, String database) {

    var address = container.getHost();
    var port = container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT);

    var configuration = JAsyncUtils.buildConfiguration(DbTestConfig.dbUser,
        DbTestConfig.dbPassword,
        address,
        port,
        database);

    var connectionPoolConfiguration = JAsyncUtils.buildPoolConfig(
        configuration,
        PoolConfiguration.Companion.getDefault());

    return new ConnectionPool<>(new PostgreSQLConnectionFactory(configuration), connectionPoolConfiguration);
  }
}
