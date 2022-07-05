import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/minimal_user_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/phone_number_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/messenger_resource.dart';

class UserResource extends MinimalUserResource {
  final List<PhoneNumberResource> phoneNumbers;
  final List<MessengerResource> messengers;
  final int created;
  final int modified;

  UserResource(int id, String email, String firstName, String secondName, String thirdName, String birthday,
      this.phoneNumbers, this.messengers, this.created, this.modified)
      : super(id, email, firstName, secondName, thirdName, birthday);

  UserResource.fromJson(Map<String, dynamic> json)
      : phoneNumbers = PhoneNumberResource.fromJsonList(json['phoneNumbers']),
        messengers = MessengerResource.fromJsonList(json['messengers']),
        created = json['created'],
        modified = json['modified'],
        super.fromJson(json);

  @override
  void buildJson(Map<String, dynamic> json) {
    super.buildJson(json);
    json['phoneNumbers'] = JsonResource.toJsonList(phoneNumbers);
    json['messengers'] = JsonResource.toJsonList(messengers);
    json['created'] = created;
    json['modified'] = modified;
  }
}
