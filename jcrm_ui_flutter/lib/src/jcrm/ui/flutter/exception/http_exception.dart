import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/model/http_error.dart';

class HttpException implements Exception {
  final HttpError error;
  HttpException(this.error);
}
