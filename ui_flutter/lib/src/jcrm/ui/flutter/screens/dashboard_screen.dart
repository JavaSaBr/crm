import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/dashborad/screen/organization_settings_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/page/settings.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/route_state.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/global_toolbar_ui_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/util/routes.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/fade_transition_page.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/global_toolbar.dart';
import 'package:provider/provider.dart';

class DashboardScreen extends StatelessWidget {
  static const routeName = "dashboard";

  static final emptyPage = MapEntry("empty", (BuildContext context) {
    return FadeTransitionPage<void>(
      key: const ValueKey('empty'),
      child: Container(),
    );
  });

  static final Map<String, Function> pages = {
    DashboardRoutes.organizationSettingsRoute: (BuildContext context) {
      return const FadeTransitionPage<void>(
        key: ValueKey(OrganizationSettingsScreen.routeName),
        child: OrganizationSettingsScreen(),
      );
    },
    '/settings': (BuildContext context) {
      return const FadeTransitionPage<void>(
        key: ValueKey('settings'),
        child: SettingsScreen(),
      );
    }
  };

  static final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  const DashboardScreen({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    print("Build Dashboard");
    RouteState routeState = RouteStateScope.of(context);
    var globalToolbarUiService = context.read<GlobalToolbarUiService>();
    return Scaffold(
      appBar: AppBar(
        title: globalToolbarUiService.toolbar,
        elevation: 15,
      ),
      body: Navigator(
        key: navigatorKey,
        onPopPage: (route, dynamic result) => route.didPop(result),
        pages: [
          buildTargetPage(routeState, context)
        ],
      ),
      drawer: Drawer(
        child: buildLeftMenuItems(routeState),
      ),
    );
  }

  ListView buildLeftMenuItems(RouteState routeState) {

    final drawerHeader = UserAccountsDrawerHeader(
      accountName: Text(
        "user name",
      ),
      accountEmail: Text(
        "user email",
      ),
      currentAccountPicture: const CircleAvatar(
        child: FlutterLogo(size: 42.0),
      ),
    );

    return ListView(
      children: [
        drawerHeader,
        ListTile(
          title: Text(
            "Org settings",
          ),
          leading: const Icon(Icons.settings),
          onTap: () {
            routeState.go(DashboardRoutes.organizationSettingsRoute);
          },
        ),
        ListTile(
          title: Text(
            "Books",
          ),
          leading: const Icon(Icons.favorite),
          onTap: () {
            routeState.go('/books');
          },
        ),
        ListTile(
          title: Text(
            "Settings",
          ),
          leading: const Icon(Icons.comment),
          onTap: () {
            routeState.go('/settings');
          },
        ),
      ],
    );
  }

  FadeTransitionPage<void> buildTargetPage(
      RouteState routeState, BuildContext context) {
    String pathTemplate = routeState.route.pathTemplate;
    return pages.entries
        .firstWhere((element) => pathTemplate.startsWith(element.key),
            orElse: () => emptyPage)
        .value(context);
  }
}
