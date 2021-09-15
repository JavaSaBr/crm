import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing/route_state.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/screens/authentication_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/screens/dashboard_screen.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/fade_transition_page.dart';

class RootNavigatorScreen extends StatefulWidget {
  final GlobalKey<NavigatorState> navigatorKey;

  const RootNavigatorScreen({
    required this.navigatorKey,
    Key? key,
  }) : super(key: key);

  @override
  RootNavigatorScreenState createState() => RootNavigatorScreenState();
}

class RootNavigatorScreenState extends State<RootNavigatorScreen> {
  final _loginKey = const ValueKey("Login screen");
  final _dashboardKey = const ValueKey<String>("Dashboard screen");

  @override
  Widget build(BuildContext context) {
    final routeState = RouteStateScope.of(context);
    final securityScope = SecurityAuthScope.of(context);
    // final pathTemplate = routeState.route.pathTemplate;
    //
    // Book? selectedBook;
    // if (pathTemplate == '/book/:bookId') {
    //   selectedBook =
    //       libraryInstance.allBooks.firstWhereOrNull((b) => b.id.toString() == routeState.route.parameters['bookId']);
    // }
    //
    // Author? selectedAuthor;
    // if (pathTemplate == '/author/:authorId') {
    //   selectedAuthor = libraryInstance.allAuthors
    //       .firstWhereOrNull((b) => b.id.toString() == routeState.route.parameters['authorId']);
    // }

    return Navigator(
      key: widget.navigatorKey,
      onPopPage: (route, dynamic result) {
        // When a page that is stacked on top of the scaffold is popped, display
        // the /books or /authors tab in BookstoreScaffold.
        // if (route.settings is Page && (route.settings as Page).key == _bookDetailsKey) {
        //   routeState.go('/books/popular');
        // }
        //
        // if (route.settings is Page && (route.settings as Page).key == _authorDetailsKey) {
        //   routeState.go('/authors');
        // }

        return route.didPop(result);
      },
      pages: [
        if (routeState.route.pathTemplate == '/login')
          // Display the sign in screen.
          FadeTransitionPage<void>(
            key: _loginKey,
            child: AuthenticationScreen(
              credentials: (credentials) async {
                var signedIn = await securityScope.login(credentials.username, credentials.password);
                if (signedIn) {
                  routeState.go(DashboardScreen.routeName);
                }
              },
            ),
          )
        else ...[
          // Display the app
          FadeTransitionPage<void>(
            key: _dashboardKey,
            child: const DashboardScreen(),
          )
        ],
      ],
    );
  }
}
