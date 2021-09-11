import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';

import 'http_service.dart';

class SecurityService extends ChangeNotifier {

  static const max_refreshed_token_message = 2001;

  static const empty_token = '';

  static const local_storage_token = 'jcrm:auth:token';
  static const header_token = 'JCRM-Access-Token';

  final HttpService _httpService;

  bool _authenticated = false;

  ReadonlyUser _currentUser = ReadonlyUser.none;

  String _accessToken = "";

  SecurityService(this._httpService);

  bool get authenticated => _authenticated;

  ReadonlyUser get currentUser => _currentUser;

  String get accessToken => _accessToken;

  void internalAuthenticate(ReadonlyUser user, String accessToken) {
    _accessToken = accessToken;
    _currentUser = user;
    _authenticated = accessToken.isNotEmpty;
    notifyListeners();
  }

  Future<void> logout() async {
    await Future<void>.delayed(const Duration(milliseconds: 200));
    internalAuthenticate(ReadonlyUser.none, empty_token);
  }

  Future<bool> login(String username, String password) async {
    await Future<void>.delayed(const Duration(milliseconds: 200));
    _authenticated = true;
    notifyListeners();
    return _authenticated;
  }
}

class SecurityAuthScope extends InheritedNotifier<SecurityService> {

  const SecurityAuthScope({
    required SecurityService notifier,
    required Widget child,
    Key? key,
  }) : super(key: key, notifier: notifier, child: child);

  static SecurityService of(BuildContext context) => context
      .dependOnInheritedWidgetOfExactType<SecurityAuthScope>()!
      .notifier!;
}