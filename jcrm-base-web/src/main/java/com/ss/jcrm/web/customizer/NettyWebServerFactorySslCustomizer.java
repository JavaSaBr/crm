package com.ss.jcrm.web.customizer;

import static com.ss.rlib.common.util.array.ArrayFactory.toArray;
import com.ss.rlib.common.util.array.ArrayFactory;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.SslServerCustomizer;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

import java.nio.file.Files;
import java.nio.file.Paths;

@AllArgsConstructor
public class NettyWebServerFactorySslCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    private final String keystore;
    private final String password;
    private final String keyAlias;

    @Override
    public void customize(@NotNull NettyReactiveWebServerFactory serverFactory) {

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
        ssl.setEnabledProtocols(toArray("TLSv1.2","TLSv1.3"));

        var http2 = new Http2();
        http2.setEnabled(true);

        serverFactory.addServerCustomizers(new SslServerCustomizer(ssl, http2, null) {

            @Override
            public @NotNull HttpServer apply(@NotNull HttpServer server) {
                return super.apply(server)
                    // it doesn't work without manually setting protocols
                    .protocol(HttpProtocol.H2, HttpProtocol.HTTP11);
            }
        });
    }
}
