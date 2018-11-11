package com.ss.jcrm.web.config;

import com.ss.jcrm.web.util.WebUtils;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ServiceConfig {

    public static @NotNull CompletableFuture<JsonObject> loadConfig(@NotNull Vertx vertx) {

        var dirStore = new ConfigStoreOptions()
                .setType("directory")
                .setConfig(new JsonObject().put("path", "config")
                        .put("filesets", new JsonArray()
                                .add(new JsonObject().put("pattern", "config/*json"))
                        ));

        var retrieverOptions = new ConfigRetrieverOptions()
                .addStore(dirStore);

        var result = new CompletableFuture<JsonObject>();

        var retriever = ConfigRetriever.create(vertx, retrieverOptions);
        retriever.getConfig(asyncResult -> WebUtils.apply(asyncResult, result));

        return result;
    }
}
