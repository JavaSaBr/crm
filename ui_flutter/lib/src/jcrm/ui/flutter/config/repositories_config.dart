import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';

class RepositoriesConfig extends MultiProvider {
  RepositoriesConfig({super.key})
      : super(providers: [
          Provider(create: (context) => UserRepository(context.read(), context.read()))
        ]);
}
