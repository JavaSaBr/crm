import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/dashborad/screen/organization_settings_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/organization/settings/scene/user_group_settings_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/organization/settings/scene/user_settings_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/screens/authentication_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/screens/dashboard_screen.dart';

class Routes {
  static const loginRoute = '/${AuthenticationScreen.routeName}';
  static const dashboardRoute = '/${DashboardScreen.routeName}';

  static final List<String> allowedPaths = [
    loginRoute,
    dashboardRoute,
    DashboardRoutes.organizationSettingsRoute,
    OrganizationSettingsRoutes.userSettingsRoute,
    OrganizationSettingsRoutes.userGroupSettingsRoute,
    "/books", "/settings"];
}

class DashboardRoutes {

  static const organizationSettingsRoute = '${Routes.dashboardRoute}/${OrganizationSettingsScreen.routeName}';
}

class OrganizationSettingsRoutes {

  static const userSettingsRoute = '${DashboardRoutes.organizationSettingsRoute}/${UserSettingsScreen.routeName}';
  static const userGroupSettingsRoute = '${DashboardRoutes.organizationSettingsRoute}/${UserGroupSettingsScreen.routeName}';
}
