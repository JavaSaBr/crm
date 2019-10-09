package com.ss.jcrm.web.exception;

public interface CommonErrors {

    int ID_NOT_PRESENTED = 5000;
    String ID_NOT_PRESENTED_MESSAGE = "Id not presented";

    int NAME_NOT_PRESENTED = 5001;
    String NAME_NOT_PRESENTED_MESSAGE = "Name not presented";

    int OFFSET_NOT_PRESENTED = 5002;
    String OFFSET_NOT_PRESENTED_MESSAGE = "Offset not presented";

    int PAGE_SIZE_NOT_PRESENTED = 5003;
    String PAGE_SIZE_NOT_PRESENTED_MESSAGE = "Page size not presented";

    int RESOURCE_IS_ALREADY_CHANFED = 5004;
    String RESOURCE_IS_ALREADY_CHANFED_MESSAGE = "Resource is already changed";
}
