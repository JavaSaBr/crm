import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class MessengerResource extends JsonResource {

  static List<MessengerResource> fromJsonList(List<dynamic> json) {
    return JsonResource.fromJsonList(json, (toRead) => MessengerResource.fromJson(toRead));
  }

  final String login;
  final int type;

  MessengerResource(this.login, this.type);

  MessengerResource.fromJson(Map<String, dynamic> json)
      : login = json['login'],
        type = json['type'];

  @override
  void buildJson(Map<String, dynamic> json) {
    json['login'] = login;
    json['type'] = type;
  }
}
