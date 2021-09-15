import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/entity_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

class UserRepository extends EntityRepository<User, UserResource> {

  UserRepository(HttpService httpService, SecurityService securityService) : super(httpService, securityService);

  @override
  String buildFindByIdUrl(int id) {
    // TODO: implement buildFindByIdUrl
    throw UnimplementedError();
  }

  @override
  User convertFromResource(UserResource resource) {
    // TODO: implement convertFromResource
    throw UnimplementedError();
  }

  @override
  UserResource convertToResource(User entity) {
    // TODO: implement convertToResource
    throw UnimplementedError();
  }

  @override
  UserResource readFromJson(Map<String, dynamic> json) {
    return UserResource.fromJson(json);
  }
}