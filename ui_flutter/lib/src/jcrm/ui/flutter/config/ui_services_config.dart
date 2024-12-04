import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/global_toolbar_ui_service.dart';
import 'package:provider/provider.dart';

class UiServicesConfig extends MultiProvider {
  UiServicesConfig({super.key})
      : super(providers: [
          Provider(create: (context) => GlobalToolbarUiService())
        ]);
}
