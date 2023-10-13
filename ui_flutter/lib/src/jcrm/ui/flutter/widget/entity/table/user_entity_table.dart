import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/sort/uniq_entity_sortable_field.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/user_table_data_source.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/entity_table.dart';

class UserTable extends EntityTable<User> {
  const UserTable({super.key});

  @override
  State<EntityTable<User>> createState() {
    return UserTableState();
  }
}

class UserTableState extends EntityTableState<User, UserTableDataSource> {

  @override
  get restorationPrefix => "user_table_list_view";

  @override
  UserTableDataSource createDataSource() {
    return UserTableDataSource(context.read<UserRepository>());
  }

  @override
  List<DataColumn> buildColumns() {
    return [
      buildColumn("Id"),
      buildColumn("First name"),
      buildColumn("Second name"),
      buildColumn("Third name"),
      buildColumn("Email"),
      buildColumn("Birthday")
    ];
  }

  @override
  SortableField? columnIndexToSortableField(int index) {
    return UniqEntitySortableField.id;
  }
}
