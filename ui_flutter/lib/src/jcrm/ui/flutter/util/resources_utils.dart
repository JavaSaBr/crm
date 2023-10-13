import 'package:intl/intl.dart';

class ResourcesUtils {

  static final DateFormat birthdayFormat = DateFormat('yyyy-MM-dd');

  static List<Map<String, dynamic>> asJsonArray(dynamic object) {
    var array = object as List<dynamic>;
    return array
        .map((e) => e as Map<String, dynamic>)
        .toList(growable: false);
  }

  static String? birthdayToString(DateTime? birthday) {
    if (birthday != null) {
      return birthdayFormat.format(birthday);
    } else {
      return null;
    }
  }
}
