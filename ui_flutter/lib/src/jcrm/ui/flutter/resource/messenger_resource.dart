import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/messenger.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class MessengerResource extends EntityResource {

  static List<MessengerResource> fromJsonList(List<dynamic> json) {
    return JsonResource.fromJsonList(json, (toRead) => MessengerResource.fromJson(toRead));
  }

  final String login;
  final int type;

  MessengerResource.fromEntity(Messenger messenger)
      : login = messenger.login,
        type = messenger.type;
  
  MessengerResource.fromJson(Map<String, dynamic> json)
      : login = json['login'],
        type = json['type'];

  MessengerResource(this.login, this.type);
  
  @override
  void buildJson(Map<String, dynamic> json) {
    json['login'] = login;
    json['type'] = type;
  }
}
