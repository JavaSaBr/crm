package com.ss.jcrm.dictionary.web.handler;

import com.ss.jcrm.dictionary.web.resource.AllCountriesOutResource;
import com.ss.jcrm.dictionary.web.resource.CountryOutResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.web.exception.IdNotPresentedWebException;
import com.ss.jcrm.web.exception.NameNotPresentedWebException;
import com.ss.rlib.common.util.Utils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CountryHandler {

    private final CachedDictionaryService<CountryOutResource, AllCountriesOutResource> countryDictionaryService;

    public @NotNull Mono<ServerResponse> getAll(@NotNull ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .syncBody(countryDictionaryService.getAll());
    }

    public @NotNull Mono<ServerResponse> getById(@NotNull ServerRequest request) {

        return Mono.fromSupplier(() -> request.pathVariable("id"))
            .flatMap(param -> Mono.justOrEmpty(Utils.<String, Long>safeGet(param, Long::valueOf)))
            .switchIfEmpty(Mono.error(IdNotPresentedWebException::new))
            .flatMap(id -> Mono.justOrEmpty(countryDictionaryService.getById(id)))
            .flatMap(country -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(country))
            .switchIfEmpty(ServerResponse.notFound()
                .build());
    }

    public @NotNull Mono<ServerResponse> getByName(@NotNull ServerRequest request) {

        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .switchIfEmpty(Mono.error(NameNotPresentedWebException::new))
            .flatMap(name -> Mono.justOrEmpty(countryDictionaryService.getByName(name)))
            .flatMap(country -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(country))
            .switchIfEmpty(ServerResponse.notFound()
                .build());
    }
}
