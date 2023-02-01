import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

class RepositoriesConfig extends MultiProvider {
  RepositoriesConfig({super.key})
      : super(providers: [
          Provider(create: (context) => UserRepository(
              context.read<HttpService>(),
              context.read<SecurityService>()))
        ]);
}
