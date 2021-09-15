import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/minimal_user_resource.dart';

class UserResource extends MinimalUserResource {
  final List<String> phoneNumbers;
  final List<String> messengers;
  final int created;
  final int modified;

  UserResource(int id, String email, String firstName, String secondName, String thirdName, String birthday,
      this.phoneNumbers, this.messengers, this.created, this.modified)
      : super(id, email, firstName, secondName, thirdName, birthday);

  UserResource.fromJson(Map<String, dynamic> json)
      : phoneNumbers = json['phoneNumbers'],
        messengers = json['messengers'],
        created = json['created'],
        modified = json['modified'],
        super.fromJson(json);

  @override
  void buildJson(Map<String, dynamic> json) {
    super.buildJson(json);
    json['phoneNumbers'] = phoneNumbers;
    json['messengers'] = messengers;
    json['created'] = created;
    json['modified'] = modified;
  }
}
