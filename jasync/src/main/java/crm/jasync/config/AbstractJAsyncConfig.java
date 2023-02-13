package crm.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import io.netty.channel.EventLoopGroup;
import crm.jasync.util.JAsyncUtils;
import java.util.concurrent.ExecutorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractJAsyncConfig {

  @NotNull ApplicationContext applicationContext;
  @NotNull String propertyPrefix;

  protected @NotNull String schema() {
    var env = applicationContext.getBean(Environment.class);
    return env.getRequiredProperty("%s.schema".formatted(propertyPrefix));
  }

  protected @NotNull Flyway flyway() {
    var env = applicationContext.getBean(Environment.class);
    var resourcePrefix = propertyPrefix.replace('.', '/');
    return Flyway
        .configure()
        .locations("classpath:%s/migration".formatted(resourcePrefix))
        .baselineOnMigrate(true)
        .schemas(env.getRequiredProperty("%s.schema".formatted(propertyPrefix)))
        .dataSource(
            env.getRequiredProperty("%s.url".formatted(propertyPrefix)),
            env.getRequiredProperty("%s.username".formatted(propertyPrefix)),
            env.getRequiredProperty("%s.password".formatted(propertyPrefix)))
        .load();
  }

  protected @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool() {

    var env = applicationContext.getBean(Environment.class);
    var eventLoopGroup = applicationContext.getBean("dbEventLoopGroup", EventLoopGroup.class);
    var poolConfiguration = applicationContext.getBean("dbPoolConfiguration", PoolConfiguration.class);
    var dbExecutor = applicationContext.getBean("dbExecutor", ExecutorService.class);

    var configuration = JAsyncUtils.buildConfiguration(
        env.getRequiredProperty("%s.username".formatted(propertyPrefix)),
        env.getRequiredProperty("%s.password".formatted(propertyPrefix)),
        env.getRequiredProperty("%s.host".formatted(propertyPrefix)),
        env.getRequiredProperty("%s.port".formatted(propertyPrefix), int.class),
        env.getRequiredProperty("%s.database".formatted(propertyPrefix)));

    var connectionPoolConfiguration = JAsyncUtils.buildPoolConfig(configuration,
        poolConfiguration,
        eventLoopGroup,
        dbExecutor);

    return new ConnectionPool<>(
        new PostgreSQLConnectionFactory(configuration),
        connectionPoolConfiguration);
  }
}
