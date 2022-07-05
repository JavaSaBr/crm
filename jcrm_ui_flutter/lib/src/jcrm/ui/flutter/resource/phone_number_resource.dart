import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class PhoneNumberResource extends JsonResource {

  static List<PhoneNumberResource> fromJsonList(List<dynamic> json) {
    return JsonResource.fromJsonList(json, (toRead) => PhoneNumberResource.fromJson(toRead));
  }

  final String countryCode;
  final String regionCode;
  final String phoneNumber;

  final int type;

  PhoneNumberResource(this.countryCode, this.regionCode, this.phoneNumber, this.type);

  PhoneNumberResource.fromJson(Map<String, dynamic> json)
      : countryCode = json['countryCode'],
        regionCode = json['regionCode'],
        phoneNumber = json['phoneNumber'],
        type = json['type'];

  @override
  void buildJson(Map<String, dynamic> json) {
    json['countryCode'] = countryCode;
    json['regionCode'] = regionCode;
    json['phoneNumber'] = phoneNumber;
    json['type'] = type;
  }
}
