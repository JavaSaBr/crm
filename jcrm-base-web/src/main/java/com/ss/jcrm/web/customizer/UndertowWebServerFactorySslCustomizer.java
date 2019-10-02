package com.ss.jcrm.web.customizer;

import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import java.nio.file.Files;
import java.nio.file.Paths;

@AllArgsConstructor
public class UndertowWebServerFactorySslCustomizer implements
    WebServerFactoryCustomizer<UndertowReactiveWebServerFactory> {

    private final String keystore;
    private final String password;
    private final String keyAlias;

    @Override
    public void customize(@NotNull UndertowReactiveWebServerFactory serverFactory) {

        var keystorePath = Paths.get(keystore);

        if (!Files.exists(keystorePath)) {
            return;
        }

        var ssl = new Ssl();
        ssl.setEnabled(true);
        ssl.setKeyStore(keystorePath.toUri().toString());
        ssl.setKeyAlias(keyAlias);
        ssl.setKeyStoreType("PKCS12");
        ssl.setKeyStorePassword(password);
        ssl.setEnabledProtocols(toArray("TLSv1.2", "TLSv1.3"));

        var http2 = new Http2();
        http2.setEnabled(false);

        serverFactory.setSsl(ssl);
        serverFactory.setHttp2(http2);
    }
}
