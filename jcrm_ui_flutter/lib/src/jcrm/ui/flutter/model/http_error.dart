import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class HttpError implements JsonResource {
  final int errorCode;
  final String errorMessage;

  HttpError(this.errorCode, this.errorMessage);

  HttpError.fromJson(Map<String, dynamic> json)
      : errorCode = json['errorCode'],
        errorMessage = json['errorMessage'];

  @override
  void buildJson(Map<String, dynamic> json) {
    json['errorCode'] = errorCode;
    json['errorMessage'] = errorMessage;
  }

  @override
  Map<String, dynamic> toJson() {
    throw UnimplementedError();
  }
}
