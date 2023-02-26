import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'src/jcrm/ui/flutter/config/application_config.dart';
import 'src/jcrm/ui/flutter/app/application_widget.dart';
import 'src/jcrm/ui/flutter/service/security_service.dart';

import 'package:flutter_web_plugins/flutter_web_plugins.dart';

void main() {
  setUrlStrategy(PathUrlStrategy());
  runApp(ApplicationConfig((context, child) {
    return ApplicationWidget(context.watch<SecurityService>());
  }));
}
