package com.ss.jcrm.jdbc.config;

import static com.ss.jcrm.base.utils.CommonUtils.CORES;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.*;

@Configuration
@AllArgsConstructor(onConstructor_ = @Autowired)
public class JdbcConfig {

    private final Environment env;

    @Bean
    @NotNull Executor fastDbTaskExecutor() {
        var threads = env.getProperty("jdbc.fast.db.task.executor.threads", int.class, CORES);
        return Executors.newFixedThreadPool(threads);
    }

    @Bean
    @NotNull Executor slowDbTaskExecutor() {
        return new ThreadPoolExecutor(
            env.getProperty("jdbc.slow.db.task.executor.min.threads", int.class, 2),
            env.getProperty("jdbc.slow.db.task.executor.max.threads", int.class, CORES * 3),
            env.getProperty("jdbc.slow.db.task.executor.keep.alive", int.class, 120),
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
