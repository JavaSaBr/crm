package jasync.config;

import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.util.DaemonThreadsFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
public class CommonJAsyncConfig {

  @Bean
  @NotNull PoolConfiguration dbPoolConfiguration() {
    return new PoolConfiguration(
        100,                         // maxObjects
        TimeUnit.MINUTES.toMillis(15),  // maxIdle
        10_000,                                 // maxQueueSize
        TimeUnit.SECONDS.toMillis(30)); // validationInterval
  }

  @Bean
  @NotNull EventLoopGroup dbEventLoopGroup() {
    return new NioEventLoopGroup(0, new DaemonThreadsFactory("db-sql-netty"));
  }

  @Bean
  @NotNull ExecutorService dbExecutor() {
    return ForkJoinPool.commonPool();
  }
}
