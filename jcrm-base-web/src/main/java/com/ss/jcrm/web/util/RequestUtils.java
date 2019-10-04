package com.ss.jcrm.web.util;

import com.ss.jcrm.web.exception.IdNotPresentedWebException;
import com.ss.jcrm.web.exception.OffsetNotPresentedWebException;
import com.ss.jcrm.web.exception.PageSizeNotPresentedWebException;
import com.ss.jcrm.web.resources.DataPageRequest;
import com.ss.rlib.common.util.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class RequestUtils {

    public static @NotNull Mono<DataPageRequest> pageRequest(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> {

            int offset = request.queryParam("offset")
                .map(NumberUtils::safeToInt)
                .orElseThrow(OffsetNotPresentedWebException::new);

            int pageSize = request.queryParam("pageSize")
                .map(NumberUtils::safeToInt)
                .orElseThrow(PageSizeNotPresentedWebException::new);

            return new DataPageRequest(offset, pageSize);
        });
    }

    public static @NotNull Mono<Long> idRequest(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> NumberUtils.toOptionalLong(request.pathVariable("id"))
            .orElseThrow(IdNotPresentedWebException::new));
    }
}
