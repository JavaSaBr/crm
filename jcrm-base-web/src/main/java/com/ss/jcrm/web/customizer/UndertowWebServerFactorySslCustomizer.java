package com.ss.jcrm.web.customizer;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import java.nio.file.Files;
import java.nio.file.Paths;

public record UndertowWebServerFactorySslCustomizer(
    @NotNull String keystore,
    @NotNull String password,
    @NotNull String keyAlias
) implements WebServerFactoryCustomizer<UndertowReactiveWebServerFactory> {

    private static final Logger log = LoggerFactory.getLogger(UndertowWebServerFactorySslCustomizer.class);

    @Override
    public void customize(@NotNull UndertowReactiveWebServerFactory serverFactory) {

        var keystorePath = Paths.get(keystore);

        if (!Files.exists(keystorePath)) {
            return;
        }

        log.info("Use SSL Certificate: {}", keystorePath);

        var ssl = new Ssl();
        ssl.setEnabled(true);
        ssl.setKeyStore(keystorePath.toUri().toString());
        ssl.setKeyAlias(keyAlias);
        ssl.setKeyStoreType("PKCS12");
        ssl.setKeyStorePassword(password);

        var http2 = new Http2();
        http2.setEnabled(true);

        serverFactory.setSsl(ssl);
        serverFactory.setHttp2(http2);
    }
}
