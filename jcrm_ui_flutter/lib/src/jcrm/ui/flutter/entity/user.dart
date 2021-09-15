import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/minimal_user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';

class User extends MinimalUser {

  static final User none = User(0, "", "", "", "", DateTime.now(), [], [], DateTime.now(), DateTime.now());

  List<String> phoneNumbers;
  List<String> messengers;

  DateTime created;
  DateTime modified;

  User(
      int id,
      String email,
      String firstName,
      String secondName,
      String thirdName,
      DateTime birthday,
      this.phoneNumbers,
      this.messengers,
      this.created,
      this.modified
  ) : super(id, email, firstName, secondName, thirdName, birthday);

  User.fromResource(UserResource resource)
      : phoneNumbers = resource.phoneNumbers,
        messengers = resource.messengers,
        created = DateTime.fromMillisecondsSinceEpoch(resource.created),
        modified = DateTime.fromMillisecondsSinceEpoch(resource.modified),
        super.fromResource(resource);
}
