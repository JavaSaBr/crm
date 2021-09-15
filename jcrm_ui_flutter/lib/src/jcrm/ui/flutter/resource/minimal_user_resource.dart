import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/uniq_entity_resource.dart';

class MinimalUserResource extends UniqEntityResource {
  final String email;
  final String firstName;
  final String secondName;
  final String thirdName;
  final String birthday;

  MinimalUserResource(int id, this.email, this.firstName, this.secondName, this.thirdName, this.birthday) : super(id);

  MinimalUserResource.fromJson(Map<String, dynamic> json)
      : email = json['email'],
        firstName = json['firstName'],
        secondName = json['secondName'],
        thirdName = json['thirdName'],
        birthday = json['birthday'],
        super.fromJson(json);

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
