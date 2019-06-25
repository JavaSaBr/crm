package com.ss.jcrm.web.exception;

public class NameNotPresentedWebException extends BadRequestWebException {

    public NameNotPresentedWebException() {
        super(CommonErrors.ID_NOT_PRESENTED_MESSAGE, CommonErrors.ID_NOT_PRESENTED);
    }
}
