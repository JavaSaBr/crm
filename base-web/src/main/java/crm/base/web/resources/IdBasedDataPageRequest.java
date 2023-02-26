package crm.base.web.resources;

public record IdBasedDataPageRequest(
    long id,
    int offset,
    int pageSize
) {}
