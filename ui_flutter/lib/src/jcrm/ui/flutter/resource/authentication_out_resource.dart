import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class AuthenticationOutResource extends JsonResource {
  final String login;
  final String password;

  AuthenticationOutResource(this.login, this.password);

  AuthenticationOutResource.fromJson(Map<String, dynamic> json)
      : login = json['login'],
        password = json['password'];

  @override
  void buildJson(Map<String, dynamic> json) {
    json['login'] = login;
    json['password'] = password;
  }
}
