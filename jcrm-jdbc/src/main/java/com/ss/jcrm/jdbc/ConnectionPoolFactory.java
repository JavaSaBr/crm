package com.ss.jcrm.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

@UtilityClass
public class ConnectionPoolFactory {

    public @NotNull DataSource newDataSource(@NotNull String url, @NotNull String user, @NotNull String password) {

        var config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    public @NotNull DataSource newDataSource(
        @NotNull String url,
        @NotNull String schema,
        @NotNull String user,
        @NotNull String password
    ) {

        var config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setSchema(schema);

        return new HikariDataSource(config);
    }
}
