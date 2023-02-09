package com.ss.jcrm.dictionary.web.handler;

import com.ss.jcrm.dictionary.web.resource.CountryOutResource;
import com.ss.jcrm.dictionary.web.service.CachedDictionaryService;
import crm.base.web.exception.IdNotPresentedWebException;
import crm.base.web.exception.NameNotPresentedWebException;
import crm.base.web.util.ResponseUtils;
import com.ss.rlib.common.util.NumberUtils;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Value
public class CountryHandler {

    @NotNull CachedDictionaryService<CountryOutResource, CountryOutResource[]> countryDictionaryService;

    public @NotNull Mono<ServerResponse> getAll(@NotNull ServerRequest request) {
        return ResponseUtils.ok(countryDictionaryService.getAll());
    }

    public @NotNull Mono<ServerResponse> getById(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> request.pathVariable("id"))
            .mapNotNull(NumberUtils::safeToLong)
            .switchIfEmpty(Mono.error(IdNotPresentedWebException::new))
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
