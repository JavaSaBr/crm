import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/phone_number_resource.dart';

class PhoneNumber extends Entity {

  final String countryCode;
  final String regionCode;
  final String phoneNumber;

  final int type;

  PhoneNumber(this.countryCode, this.regionCode, this.phoneNumber, this.type);

  PhoneNumber.fromResource(PhoneNumberResource resource)
      : countryCode = resource.countryCode,
        regionCode = resource.regionCode,
        phoneNumber = resource.phoneNumber,
        type = resource.type;
}