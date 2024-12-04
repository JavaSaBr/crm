import 'package:flutter/material.dart';

class UserGroupSettingsScreen extends StatelessWidget {
  static const routeName = "user-group-settings";

  const UserGroupSettingsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    print("Build UserGroupSettingsScreen");
    return const Text("User group settings");
  }
}
