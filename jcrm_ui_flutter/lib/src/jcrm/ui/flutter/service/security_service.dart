import 'dart:html';

import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/env_config.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/authentication_in_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/authentication_out_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/error_service.dart';

import '../exception/http_exception.dart';
import 'http_service.dart';

class SecurityService extends ChangeNotifier {

  static const headerToken = 'jcrm-access-token';
  static const maxRefreshedTokenMessage = 2001;
  static const emptyToken = '';
  static const localStorageToken = 'jcrm:auth:token';

  final HttpService _httpService;
  final Storage _localStorage;

  bool _authenticated = false;
  User _currentUser = User.none;
  String _accessToken = "";

  SecurityService(this._httpService, this._localStorage);

  Future<bool> tryToRestoreSession() async {
   
    var restoredToken = _localStorage[localStorageToken];

    if (restoredToken == null) {
      return false;
    }

    var url = "${EnvConfig.registrationUrl}/authenticate/$restoredToken";
    try {
      var response = await _httpService.get(url, (json) => AuthenticationInResource.fromJson(json), {});
      internalAuthenticate(User.fromResource(response.user), response.token);
    } catch (ex) {
      var flag = ex is HttpException;
      var tokenExpired = Errors.tokenExpired;
      if (ex is HttpException && ex.error.errorCode == Errors.tokenExpired) {
        return refreshToken(restoredToken);
      } else {
        _localStorage.remove(localStorageToken);
        internalAuthenticate(User.none, emptyToken);
      }
    }

    return _authenticated;
  }

  Future<bool> refreshToken(String token) async {

    var url = "${EnvConfig.registrationUrl}/token/refresh/$token";
    try {
      var response = await _httpService.get(url, (json) => AuthenticationInResource.fromJson(json), {});
      internalAuthenticate(User.fromResource(response.user), response.token);
      _localStorage[localStorageToken] = response.token;
    } catch (ex) {
      if (ex is HttpException && ex.error.errorCode == Errors.tokenMaxRefreshed) {
        _localStorage.remove(localStorageToken);
      }
      internalAuthenticate(User.none, emptyToken);
    }

    return _authenticated;
  }

  Future<void> logout() async {
    await Future<void>.delayed(const Duration(milliseconds: 200));
    internalAuthenticate(User.none, emptyToken);
  }

  Future<bool> login(String username, String password) async {

    var url = "${EnvConfig.registrationUrl}/authenticate";
    var body = AuthenticationOutResource(username, password);

    try {
      var response = await _httpService.post(url, body, (json) => AuthenticationInResource.fromJson(json));
      _localStorage[localStorageToken] = response.token;
      internalAuthenticate(User.fromResource(response.user), response.token);
    } catch (ex) {
      _accessToken = emptyToken;
      _currentUser = User.none;
      _authenticated = false;
      rethrow;
    }

    return _authenticated;
  }

  void internalAuthenticate(User user, String accessToken) {
    _accessToken = accessToken;
    _currentUser = user;
    _authenticated = accessToken.isNotEmpty;
    notifyListeners();
  }

  Future<T> doGet<T extends JsonResource>(String url, T Function(Map<String, dynamic> json) jsonReader) async {
    if (_accessToken.isEmpty) {
      throw AssertionError("Is not authenticated");
    } else {
      return _httpService.get(url, jsonReader, {headerToken: _accessToken});
    }
  }

  bool get authenticated => _authenticated;
  User get currentUser => _currentUser;
  String get accessToken => _accessToken;
}

class SecurityAuthScope extends InheritedNotifier<SecurityService> {
  const SecurityAuthScope({
    required SecurityService notifier,
    required Widget child,
    Key? key,
  }) : super(key: key, notifier: notifier, child: child);

  static SecurityService of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<SecurityAuthScope>()!.notifier!;
}