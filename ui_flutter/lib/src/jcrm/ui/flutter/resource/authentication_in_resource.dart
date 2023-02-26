import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';

class AuthenticationInResource extends JsonResource {
  final UserResource user;
  final String token;

  AuthenticationInResource(this.user, this.token);

  AuthenticationInResource.fromJson(Map<String, dynamic> json)
      : user = UserResource.fromJson(json['user']),
        token = json['token'];

  @override
  void buildJson(Map<String, dynamic> json) {
    json['user'] = user.toJson();
    json['token'] = token;
  }
}
