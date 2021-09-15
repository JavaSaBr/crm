import 'package:flutter/src/widgets/framework.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/entity_table.dart';

class UserTable extends EntityTable<User> {
  UserTable({Key? key}) : super(key: key, dataSource: EntityDataSource([
    User(1, "test@test", "Fist name", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(), DateTime.now()),
    User(2, "test@test", "Fist name", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(), DateTime.now()),
  ]));

  @override
  State<StatefulWidget> createState() => UserTableState(dataSource);
}

class UserTableState extends EntityTableState<User> {
  UserTableState(EntityDataSource<User> dataSource) : super(dataSource);
}
