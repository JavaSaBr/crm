import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/phone_number.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class PhoneNumberResource extends EntityResource {

  static List<PhoneNumberResource> fromJsonList(List<dynamic> json) {
    return JsonResource.fromJsonList(json, (toRead) => PhoneNumberResource.fromJson(toRead));
  }

  final String countryCode;
  final String regionCode;
  final String phoneNumber;

  final int type;

  PhoneNumberResource.fromEntity(PhoneNumber phoneNumber)
      : countryCode = phoneNumber.countryCode,
        regionCode = phoneNumber.regionCode,
        phoneNumber = phoneNumber.phoneNumber,
        type = phoneNumber.type;

  PhoneNumberResource.fromJson(Map<String, dynamic> json)
      : countryCode = json['countryCode'],
        regionCode = json['regionCode'],
        phoneNumber = json['phoneNumber'],
        type = json['type'];

  PhoneNumberResource(this.countryCode, this.regionCode, this.phoneNumber, this.type);

  @override
  void buildJson(Map<String, dynamic> json) {
    json['countryCode'] = countryCode;
    json['regionCode'] = regionCode;
    json['phoneNumber'] = phoneNumber;
    json['type'] = type;
  }
}
