package crm.security.web.exception;

public interface SecurityErrors {

  int EXPIRED_TOKEN = 2000;
  String EXPIRED_TOKEN_MESSAGE = "Token is expired";

  int MAX_REFRESHED_TOKEN = 2001;
  String MAX_REFRESHED_TOKEN_MESSAGE = "Token has reached max of refreshes";

  int INVALID_TOKEN = 2002;
  String INVALID_TOKEN_MESSAGE = "Invalid token";

  int NOT_PRESENTED_TOKEN = 2003;
  String NOT_PRESENTED_TOKEN_MESSAGE = "Token is not presented";

  int HAS_NO_REQUIRED_ACCESS_ROLE = 2004;
  String HAS_NO_REQUIRED_ACCESS_ROLE_MESSAGE = "Has no required access role for this operation";
}
