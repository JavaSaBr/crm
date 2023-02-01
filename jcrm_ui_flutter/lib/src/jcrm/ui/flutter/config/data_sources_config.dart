import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/user_data_source.dart';

class DataSourcesConfig extends MultiProvider {
  DataSourcesConfig({super.key})
      : super(providers: [
          Provider(create: (context) => UserDataSource(context.read<UserRepository>()))
        ]);
}
