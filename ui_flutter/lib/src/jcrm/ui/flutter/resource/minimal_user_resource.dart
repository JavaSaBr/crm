import 'package:intl/intl.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/minimal_user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/uniq_entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/util/resources_utils.dart';

class MinimalUserResource extends UniqEntityResource {

  final String email;
  final String? firstName;
  final String? secondName;
  final String? thirdName;
  final String? birthday;

  MinimalUserResource.fromEntity(MinimalUser entity)
      : email = entity.email,
        firstName = entity.firstName,
        secondName = entity.secondName,
        thirdName = entity.thirdName,
        birthday = ResourcesUtils.birthdayToString(entity.birthday),
        super.fromEntity(entity);

  MinimalUserResource.fromJson(Map<String, dynamic> json)
      : email = json['email'],
        firstName = json['firstName'],
        secondName = json['secondName'],
        thirdName = json['thirdName'],
        birthday = json['birthday'],
        super.fromJson(json);

  MinimalUserResource(int id, this.email, this.firstName, this.secondName, this.thirdName, this.birthday) : super(id);
  
  @override
  void buildJson(Map<String, dynamic> json) {
    super.buildJson(json);
    json['email'] = email;
    json['firstName'] = firstName;
    json['secondName'] = secondName;
    json['thirdName'] = thirdName;
    json['birthday'] = birthday;
  }
}
