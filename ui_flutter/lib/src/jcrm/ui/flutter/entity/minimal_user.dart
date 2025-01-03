import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/uniq_entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/minimal_user_resource.dart';

class MinimalUser extends UniqEntity {

  static final MinimalUser none = MinimalUser(0, "", "", "", "", DateTime.now());

  String email;
  String firstName;
  String secondName;
  String thirdName;
  DateTime? birthday;

  MinimalUser(int id, this.email, this.firstName, this.secondName, this.thirdName, this.birthday) : super(id);

  MinimalUser.fromResource(MinimalUserResource resource)
      : email = resource.email,
        firstName = resource.firstName == null? "" : resource.firstName!,
        secondName = resource.secondName == null ? "" : resource.secondName!,
        thirdName = resource.thirdName == null ? "" : resource.thirdName!,
        birthday = resource.birthday == null ? null : DateTime.parse(resource.birthday!),
        super.fromResource(resource);

  List<String> names() {

    List<String> result = [];

    if (firstName.isNotEmpty) {
      result.add(firstName);
    }
    if (secondName.isNotEmpty) {
      result.add(firstName);
    }
    if (thirdName.isNotEmpty) {
      result.add(firstName);
    }

    return result;
  }
}
