package com.ss.jcrm.web.resources;

import lombok.Value;

@Value
public class IdBasedDataPageRequest {
    long id;
    int offset;
    int pageSize;
}
