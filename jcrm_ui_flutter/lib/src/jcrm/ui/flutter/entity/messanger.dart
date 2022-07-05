import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/messenger_resource.dart';

class Messanger extends Entity {

  final String login;
  final int type;

  Messanger(this.login, this.type);

  Messanger.fromResource(MessengerResource resource)
      : login = resource.login,
        type = resource.type;
}
