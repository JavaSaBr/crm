import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/jcrm_ui_application.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/error_service.dart';
import 'package:provider/provider.dart';
import 'package:flutter_web_plugins/flutter_web_plugins.dart';
import 'src/jcrm/ui/flutter/service/http_service.dart';
import 'src/jcrm/ui/flutter/service/registration_service.dart';
import 'src/jcrm/ui/flutter/service/security_service.dart';

void main() {
  setUrlStrategy(PathUrlStrategy());
  runApp(MultiProvider(
      providers: [Provider(create: (context) => ErrorService())],
      child: MultiProvider(
          providers: [Provider(create: (context) => HttpService(context.read<ErrorService>()))],
          child: MultiProvider(
            providers: [Provider(create: (context) => RegistrationService(context.read<HttpService>()))],
            child: MultiProvider(
                providers: [
                  ChangeNotifierProvider(create: (context) => SecurityService(context.read<HttpService>())),
                ],
                builder: (context, child) {
                  return JcrmUiApplication(context.watch<SecurityService>());
                }),
          ))));
}
