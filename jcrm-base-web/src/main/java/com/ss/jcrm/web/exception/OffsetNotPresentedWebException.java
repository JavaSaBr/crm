package com.ss.jcrm.web.exception;

public class OffsetNotPresentedWebException extends BadRequestWebException {

    public OffsetNotPresentedWebException() {
        super(CommonErrors.OFFSET_NOT_PRESENTED_MESSAGE, CommonErrors.OFFSET_NOT_PRESENTED);
    }
}
