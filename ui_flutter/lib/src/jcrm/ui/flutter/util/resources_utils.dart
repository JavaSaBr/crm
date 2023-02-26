import 'package:intl/intl.dart';

class ResourcesUtils {

  static final DateFormat birthdayFormat = DateFormat('yyyy-MM-dd');

  static List<Map<String, dynamic>> asJsonArray(dynamic object) {
    return object as List<Map<String, dynamic>>;
  }

  static String? birthdayToString(DateTime? birthday) {
    if (birthday != null) {
      return birthdayFormat.format(birthday);
    } else {
      return null;
    }
  }
}
