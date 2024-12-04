import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/organization/settings/scene/user_group_settings_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/organization/settings/scene/user_settings_screen.dart';

class OrganizationSettingsScreen extends StatefulWidget {
  static const routeName = "organization-settings";

  static final Map<String, Function> tabMapping = {
    "User settings": (BuildContext context) {
      return const UserSettingsScreen();
    },
    "User group settings": (BuildContext context) {
      return const UserGroupSettingsScreen();
    }
  };

  const OrganizationSettingsScreen({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return OrganizationSettingsScreenState();
  }
}

class OrganizationSettingsScreenState extends State<OrganizationSettingsScreen>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    _tabController = TabController(
      initialIndex: 0,
      length: 2,
      vsync: this,
    );

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    var tabs = OrganizationSettingsScreen.tabMapping;
    return Scaffold(
      //backgroundColor: themeData.colorScheme.primary,
      appBar: AppBar(
        toolbarHeight: 0,
        bottom: TabBar(
          controller: _tabController,
          tabs: [
            for (final title in tabs.keys) Tab(text: title),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          for (final factory in tabs.values) factory(context),
        ],
      ),
      floatingActionButton:
          FloatingActionButton(onPressed: () {}, child: const Icon(Icons.add)),
    );
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }
}
