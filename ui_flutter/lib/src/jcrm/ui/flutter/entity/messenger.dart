import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/messenger_resource.dart';

class Messenger extends Entity {

  final String login;
  final int type;

  Messenger(this.login, this.type);

  Messenger.fromResource(MessengerResource resource)
      : login = resource.login,
        type = resource.type;
}
