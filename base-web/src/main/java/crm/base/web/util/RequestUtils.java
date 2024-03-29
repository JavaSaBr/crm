package crm.base.web.util;

import crm.base.web.exception.IdNotPresentedWebException;
import crm.base.web.exception.OffsetNotPresentedWebException;
import crm.base.web.exception.PageSizeNotPresentedWebException;
import crm.base.web.resources.DataPageRequest;
import crm.base.web.resources.IdBasedDataPageRequest;
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

    public static @NotNull Mono<IdBasedDataPageRequest> idBasedPageRequest(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> {

            var id = NumberUtils.safeToLong(request.pathVariable("id"));

            if (id == null) {
                throw new IdNotPresentedWebException();
            }

            int offset = request.queryParam("offset")
                .map(NumberUtils::safeToInt)
                .orElseThrow(OffsetNotPresentedWebException::new);

            int pageSize = request.queryParam("pageSize")
                .map(NumberUtils::safeToInt)
                .orElseThrow(PageSizeNotPresentedWebException::new);

            return new IdBasedDataPageRequest(id, offset, pageSize);
        });
    }

    public static @NotNull Mono<Long> idRequest(@NotNull ServerRequest request) {
        return Mono.fromSupplier(() -> NumberUtils.toOptionalLong(request.pathVariable("id"))
            .orElseThrow(IdNotPresentedWebException::new));
    }

    public static @NotNull Mono<long[]> idsRequest(@NotNull ServerRequest request) {
        return request.bodyToMono(long[].class)
            .filter(ids -> ids.length > 0);
    }
}
