package com.ss.jcrm.web.config;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class ApiEndpointServer {

    @NotNull String contextPath;
}
