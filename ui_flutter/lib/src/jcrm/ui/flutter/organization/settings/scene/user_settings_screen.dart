import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/global_toolbar_ui_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/user_entity_table.dart';
import 'package:provider/provider.dart';

class UserSettingsScreen extends StatelessWidget {
  static const routeName = "user-settings";

  const UserSettingsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    print("Build UserSettingsScreen");

    var globalToolbarUiService = context.read<GlobalToolbarUiService>();
    return Container(
      alignment: Alignment.bottomRight,
      child: const UserTable(),
    );
  }
}