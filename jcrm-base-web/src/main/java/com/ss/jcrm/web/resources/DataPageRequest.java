package com.ss.jcrm.web.resources;

import lombok.Data;

public @Data class DataPageRequest {
    private final int offset;
    private final int pageSize;
}
