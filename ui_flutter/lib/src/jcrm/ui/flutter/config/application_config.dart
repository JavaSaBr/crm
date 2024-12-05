import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/config/ui_services_config.dart';
import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/config/repositories_config.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/error_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/registration_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

import 'package:web/web.dart';

class ApplicationConfig extends MultiProvider {
  ApplicationConfig(TransitionBuilder appBuilder, {Key? appKey})
      : super(
            key: appKey,
            providers: [
              Provider(create: (context) => ErrorService()),
              Provider(create: (context) => HttpService(context.read())),
              Provider(create: (context) => RegistrationService(context.read())),
              ChangeNotifierProvider(create: (context) => SecurityService(context.read(), window.localStorage)),
              RepositoriesConfig(),
              UiServicesConfig()
            ],
            builder: appBuilder);
}
