package crm.base.web.resources;

public record DataPageRequest(
    int offset,
    int pageSize
) {}
