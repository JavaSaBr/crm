package com.ss.jcrm.ui.server;

import com.ss.jcrm.ui.server.config.UiServerConfig;
import com.ss.jcrm.web.BaseWebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(UiServerConfig.class)
public class UiServerApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {
        SpringApplication.run(UiServerApplication.class, args);
    }
}
