package crm.base.web.exception;

public class PageSizeNotPresentedWebException extends BadRequestWebException {
  public PageSizeNotPresentedWebException() {
    super(CommonErrors.PAGE_SIZE_NOT_PRESENTED_MESSAGE, CommonErrors.PAGE_SIZE_NOT_PRESENTED);
  }
}
