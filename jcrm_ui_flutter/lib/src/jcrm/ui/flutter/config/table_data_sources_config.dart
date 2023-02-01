import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/user_table_data_source.dart';

class TableDataSourcesConfig extends MultiProvider {
  TableDataSourcesConfig({super.key})
      : super(providers: [
          Provider(create: (context) => UserTableDataSource(context.read<UserRepository>()))
        ]);
}
