package com.ss.jcrm.client.jasync.config;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.jsoniter.spi.JsoniterSpi;
import com.ss.jcrm.base.utils.HasId;
import com.ss.jcrm.client.api.EmailType;
import com.ss.jcrm.client.api.MessengerType;
import com.ss.jcrm.client.api.PhoneNumberType;
import com.ss.jcrm.client.api.SiteType;
import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.client.jasync.dao.JAsyncSimpleContactDao;
import com.ss.jcrm.jasync.config.JAsyncConfig;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import io.netty.channel.EventLoopGroup;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;

@Configuration
@PropertySources({
    @PropertySource("classpath:com/ss/jcrm/client/jasync/client-jasync.properties"),
    @PropertySource(
        value = "classpath:com/ss/jcrm/client/jasync/client-jasync-${spring.profiles.active:default}.properties",
        ignoreResourceNotFound = true
    )
})
@Import(JAsyncConfig.class)
public class JAsyncClientConfig {

    static {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);

        synchronized (JsoniterSpi.class) {

            JsoniterSpi.registerTypeEncoder(PhoneNumberType.class,
                (obj, stream) -> stream.writeVal(((HasId) obj).getId()));
            JsoniterSpi.registerTypeEncoder(EmailType.class,
                (obj, stream) -> stream.writeVal(((HasId) obj).getId()));
            JsoniterSpi.registerTypeEncoder(MessengerType.class,
                (obj, stream) -> stream.writeVal(((HasId) obj).getId()));
            JsoniterSpi.registerTypeEncoder(SiteType.class,
                (obj, stream) -> stream.writeVal(((HasId) obj).getId()));

            JsoniterSpi.registerTypeDecoder(PhoneNumberType.class, iter -> PhoneNumberType.of(iter.readInt()));
            JsoniterSpi.registerTypeDecoder(EmailType.class, iter -> EmailType.of(iter.readInt()));
            JsoniterSpi.registerTypeDecoder(MessengerType.class, iter -> MessengerType.of(iter.readInt()));
            JsoniterSpi.registerTypeDecoder(SiteType.class, iter -> SiteType.of(iter.readInt()));
        }
    }

    @Autowired
    private Environment env;

    @Autowired
    private PoolConfiguration dbPoolConfiguration;

    @Autowired
    private EventLoopGroup dbEventLoopGroup;

    @Autowired
    private ExecutorService dbExecutor;

    @Bean
    @DependsOn("clientConnectionPool")
    @NotNull Flyway clientFlyway() {

        var flyway = Flyway.configure()
            .locations("classpath:com/ss/jcrm/client/db/migration")
            .baselineOnMigrate(true)
            .schemas(env.getRequiredProperty("jdbc.client.db.schema"))
            .dataSource(
                env.getRequiredProperty("jdbc.client.db.url"),
                env.getRequiredProperty("jdbc.client.db.username"),
                env.getRequiredProperty("jdbc.client.db.password")
            )
            .load();

        if (env.getProperty("db.upgrading.enabled", boolean.class, false)) {
            flyway.migrate();
        }

        return flyway;
    }

    @Bean
    @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool() {

        var configuration = JAsyncUtils.buildConfiguration(
            env.getRequiredProperty("jdbc.client.db.username"),
            env.getRequiredProperty("jdbc.client.db.password"),
            env.getRequiredProperty("jdbc.client.db.host"),
            env.getRequiredProperty("jdbc.client.db.port", int.class),
            env.getRequiredProperty("jdbc.client.db.database")
        );

        var connectionPoolConfiguration = JAsyncUtils.buildPoolConfig(
            configuration,
            dbPoolConfiguration,
            dbEventLoopGroup,
            dbExecutor
        );

        return new ConnectionPool<>(
            new PostgreSQLConnectionFactory(configuration),
            connectionPoolConfiguration
        );
    }

    @Bean
    @NotNull SimpleContactDao simpleContactDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> clientConnectionPool
    ) {
        return new JAsyncSimpleContactDao(clientConnectionPool, env.getRequiredProperty("jdbc.client.db.schema"));
    }
}

