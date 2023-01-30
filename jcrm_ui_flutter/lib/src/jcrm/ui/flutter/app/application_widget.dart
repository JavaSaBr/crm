import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/delegate.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/parsed_route.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/parser.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/route_state.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/screens/root_navigator_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/util/routes.dart';

class ApplicationWidget extends StatefulWidget {
  final SecurityService _securityService;

  const ApplicationWidget(this._securityService, {Key? key}) : super(key: key);

  @override
  ApplicationWidgetState createState() => ApplicationWidgetState(_securityService);
}

class ApplicationWidgetState extends State<ApplicationWidget> {
  final _navigatorKey = GlobalKey<NavigatorState>();

  final SecurityService _securityService;

  late final RouteState _routeState;
  late final SimpleRouterDelegate _routerDelegate;
  late final TemplateRouteParser _routeParser;

  ApplicationWidgetState(this._securityService);

  @override
  void initState() {
    /// Configure the parser with all of the app's allowed path templates.
    _routeParser = TemplateRouteParser(
      allowedPaths: Routes.allowedPaths,
      guard: _guard,
      initialRoute: Routes.loginRoute,
    );

    _routeState = RouteState(_routeParser);

    _routerDelegate = SimpleRouterDelegate(
      routeState: _routeState,
      navigatorKey: _navigatorKey,
      builder: (context) => RootNavigatorScreen(
        navigatorKey: _navigatorKey,
      ),
    );

    // Listen for when the user logs out and display the signin screen.
    _securityService.addListener(_handleAuthStateChanged);

    super.initState();
  }

  @override
  Widget build(BuildContext context) => RouteStateScope(
        notifier: _routeState,
        child: SecurityAuthScope(
          notifier: _securityService,
          child: MaterialApp.router(
            darkTheme: ThemeData.dark(),
            themeMode: ThemeMode.light,
            routerDelegate: _routerDelegate,
            routeInformationParser: _routeParser,
          ),
        ),
      );

  Future<ParsedRoute> _guard(ParsedRoute from) async {
    var signedIn = _securityService.authenticated;

    if (!signedIn) {
      signedIn = await _securityService.tryToRestoreSession();
    }

    final signInRoute = ParsedRoute(Routes.loginRoute, Routes.loginRoute, {}, {});

    // Go to /signin if the user is not signed in
    if (!signedIn && from != signInRoute) {
      return signInRoute;
    }
    // Go to /books if the user is signed in and tries to go to /signin.
    else if (signedIn && from == signInRoute) {
      return ParsedRoute(Routes.dashboardRoute, Routes.dashboardRoute, {}, {});
    }
    return from;
  }

  void _handleAuthStateChanged() {
    if (!_securityService.authenticated) {
      _routeState.go(Routes.loginRoute);
    }
  }

  @override
  void dispose() {
    _securityService.removeListener(_handleAuthStateChanged);
    _routeState.dispose();
    _routerDelegate.dispose();
    super.dispose();
  }
}
