import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/dashborad/screen/organization_settings_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/page/settings.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/route_state.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/util/routes.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/fade_transition_page.dart';

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

    var routeState = RouteStateScope.of(context);
    var currentRoute = routeState.route;
    var pathTemplate = currentRoute.pathTemplate;

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

    final drawerItems = ListView(
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



    return Scaffold(
      appBar: AppBar(
        title: Text(
          "App bar",
        ),
      ),
      body: Navigator(
        key: navigatorKey,
        onPopPage: (route, dynamic result) => route.didPop(result),
        pages: [

          pages.entries
              .firstWhere((element) => pathTemplate.startsWith(element.key), orElse: () => emptyPage)
              .value(context)

          // if (currentRoute.pathTemplate.startsWith('/authors'))
          //   const FadeTransitionPage<void>(
          //     key: ValueKey('authors'),
          //     child: AuthorsScreen(),
          //   )
          // else if (currentRoute.pathTemplate.startsWith('/settings'))
          //   const FadeTransitionPage<void>(
          //     key: ValueKey('settings'),
          //     child: SettingsScreen(),
          //   )
          // else if (currentRoute.pathTemplate.startsWith('/books') || currentRoute.pathTemplate == '/')
          //   const FadeTransitionPage<void>(
          //     key: ValueKey('books'),
          //     child: BooksScreen(),
          //   )
          //
          // // Avoid building a Navigator with an empty `pages` list when the
          // // RouteState is set to an unexpected path, such as /signin.
          // //
          // // Since RouteStateScope is an InheritedNotifier, any change to the
          // // route will result in a call to this build method, even though this
          // // widget isn't built when those routes are active.
          // else
          //   FadeTransitionPage<void>(
          //     key: const ValueKey('empty'),
          //     child: Container(),
          //   ),
        ],
      ),
      drawer: Drawer(
        child: drawerItems,
      ),
    );
  }
}
