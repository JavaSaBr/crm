import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/env_config.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/entity_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

class UserRepository extends EntityRepository<User, UserResource> {

  UserRepository(HttpService httpService, SecurityService securityService) : super(httpService, securityService);

  @override
  String fetchByIdUrl() {
    return "${EnvConfig.registrationUrl}/user";
  }

  @override
  String fetchPageUrl() {
    return "${EnvConfig.registrationUrl}/users/page";
  }

  @override
  User convertFromResource(UserResource resource) {
    return User.fromResource(resource);
  }

  @override
  UserResource convertToResource(User entity) {
    return UserResource.fromEntity(entity);
  }

  @override
  UserResource readFromJson(Map<String, dynamic> json) {
    return UserResource.fromJson(json);
  }
}