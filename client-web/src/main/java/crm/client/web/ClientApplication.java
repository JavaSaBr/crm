package crm.client.web;

import crm.base.web.BaseWebApplication;
import crm.client.web.config.ClientWebConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ClientWebConfig.class)
public class ClientApplication extends BaseWebApplication {

    public static void main(@NotNull String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
