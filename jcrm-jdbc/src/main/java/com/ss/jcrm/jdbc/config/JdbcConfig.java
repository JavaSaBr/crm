package com.ss.jcrm.jdbc.config;

import static com.ss.jcrm.base.utils.CommonUtils.CORES;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@AllArgsConstructor(onConstructor_ = @Autowired)
public class JdbcConfig {

    private final Environment env;

    @Bean
    @NotNull Executor fastDbTaskExecutor() {
        var threads = env.getProperty("jdbc.fast.db.task.executor.threads", Integer.class, CORES);
        return Executors.newFixedThreadPool(threads);
    }

    @Bean
    @NotNull Executor slowDbTaskExecutor() {
        var threads = env.getProperty("jdbc.slow.db.task.executor.threads", Integer.class, CORES);
        return Executors.newFixedThreadPool(threads);
    }
}
