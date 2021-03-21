package com.ss.jcrm.web.resources;

public record IdBasedDataPageRequest(
    long id,
    int offset,
    int pageSize
) {}
