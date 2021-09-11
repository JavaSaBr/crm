import 'package:flutter/material.dart';

import 'page/navigator.dart';
import 'routing.dart';
import 'service/security_service.dart';

class JcrmUiApplication extends StatefulWidget {

  final SecurityService _securityService;

  JcrmUiApplication(this._securityService, {Key? key}) : super(key: key);

  @override
  JcrmUiApplicationState createState() =>
      JcrmUiApplicationState(_securityService);
}

class JcrmUiApplicationState extends State<JcrmUiApplication> {

  final _navigatorKey = GlobalKey<NavigatorState>();

  final SecurityService _securityService;

  late final RouteState _routeState;
  late final SimpleRouterDelegate _routerDelegate;
  late final TemplateRouteParser _routeParser;

  JcrmUiApplicationState(this._securityService);

  @override
  void initState() {

    /// Configure the parser with all of the app's allowed path templates.
    _routeParser = TemplateRouteParser(
      allowedPaths: [
        '/signin',
        '/authors',
        '/settings',
        '/books/new',
        '/books/all',
        '/books/popular',
        '/book/:bookId',
        '/author/:authorId',
      ],
      guard: _guard,
      initialRoute: '/signin',
    );

    _routeState = RouteState(_routeParser);

    _routerDelegate = SimpleRouterDelegate(
      routeState: _routeState,
      navigatorKey: _navigatorKey,
      builder: (context) => BookstoreNavigator(
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
        routerDelegate: _routerDelegate,
        routeInformationParser: _routeParser,
      ),
    ),
  );

  Future<ParsedRoute> _guard(ParsedRoute from) async {
    final signedIn = _securityService.authenticated;
    final signInRoute = ParsedRoute('/signin', '/signin', {}, {});

    // Go to /signin if the user is not signed in
    if (!signedIn && from != signInRoute) {
      return signInRoute;
    }
    // Go to /books if the user is signed in and tries to go to /signin.
    else if (signedIn && from == signInRoute) {
      return ParsedRoute('/books/popular', '/books/popular', {}, {});
    }
    return from;
  }

  void _handleAuthStateChanged() {
    if (!_securityService.authenticated) {
      _routeState.go('/signin');
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
