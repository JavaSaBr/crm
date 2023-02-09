package crm.base.web.customizer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NettyWebServerFactorySslCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

  @NotNull String keystore;
  @NotNull String password;
  @NotNull String keyAlias;

  @Override
  public void customize(@NotNull NettyReactiveWebServerFactory serverFactory) {

    var keystorePath = Paths.get(keystore);

    if (!Files.exists(keystorePath)) {
      return;
    }

    var ssl = new Ssl();
    ssl.setEnabled(true);
    ssl.setKeyStore(keystorePath
        .toUri()
        .toString());
    ssl.setKeyAlias(keyAlias);
    ssl.setKeyStoreType("PKCS12");
    ssl.setKeyStorePassword(password);

    var http2 = new Http2();
    http2.setEnabled(true);

       /* serverFactory.addServerCustomizers(new SslServerCustomizer(ssl, http2, null) {

            @Override
            public @NotNull HttpServer apply(@NotNull HttpServer server) {
                return super.apply(server)
                    // it doesn't work without manually setting protocols
                    .protocol(HttpProtocol.H2, HttpProtocol.HTTP11);
            }
        });*/
  }
}
