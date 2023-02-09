package crm.base.web.exception;

public class IdNotPresentedWebException extends BadRequestWebException {
  public IdNotPresentedWebException() {
    super(CommonErrors.ID_NOT_PRESENTED_MESSAGE, CommonErrors.ID_NOT_PRESENTED);
  }
}
