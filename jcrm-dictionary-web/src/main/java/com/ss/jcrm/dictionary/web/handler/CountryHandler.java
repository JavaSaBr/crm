package com.ss.jcrm.dictionary.web.handler;

import com.ss.jcrm.dictionary.web.resource.CountryOutResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import com.ss.jcrm.web.exception.IdNotPresentedWebException;
import com.ss.jcrm.web.exception.NameNotPresentedWebException;
import com.ss.jcrm.web.util.ResponseUtils;
import com.ss.rlib.common.util.NumberUtils;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@AllArgsConstructor
public class CountryHandler {

    private final CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService;

    public @NotNull Mono<ServerResponse> getAll(@NotNull ServerRequest request) {
        return ResponseUtils.ok(countryDictionaryService.getAll());
    }

    public @NotNull Mono<ServerResponse> getById(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("id"))
            .map(NumberUtils::toOptionalLong)
            .map(optional -> optional.orElseThrow(IdNotPresentedWebException::new))
            .map(countryDictionaryService::getByIdOptional)
            .flatMap(ResponseUtils::optionalResource);
    }

    public @NotNull Mono<ServerResponse> getByName(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("name"))
            .switchIfEmpty(Mono.error(NameNotPresentedWebException::new))
            .map(countryDictionaryService::getByNameOptional)
            .flatMap(ResponseUtils::optionalResource);
    }
}
