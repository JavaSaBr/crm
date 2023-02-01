import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/config/data_sources_config.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/config/repositories_config.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/error_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/registration_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

import 'dart:html';

class ApplicationConfig extends MultiProvider {
  ApplicationConfig(TransitionBuilder appBuilder, {Key? appKey})
      : super(
            key: appKey,
            providers: [
              Provider(create: (context) => ErrorService())
            ],
            child: MultiProvider(
                providers: [
                  Provider(create: (context) => HttpService(context.read<ErrorService>()))
                ],
                child: MultiProvider(
                  providers: [
                    Provider(create: (context) => RegistrationService(context.read<HttpService>()))
                  ],
                  child: MultiProvider(
                      providers: [
                        ChangeNotifierProvider(create: (context) => SecurityService(context.read<HttpService>(), window.localStorage)),
                      ],
                      child: MultiProvider(
                        providers: [
                          RepositoriesConfig()
                        ],
                        child: MultiProvider(
                          providers: [
                            DataSourcesConfig()
                          ],
                          builder: appBuilder
                        ))))));
}
