import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/user_entity_table.dart';

class UserSettingsScreen extends StatelessWidget {
  static const routeName = "user-settings";

  const UserSettingsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return UserTable();
  }
}