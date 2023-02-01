import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/user_data_source.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/entity_table.dart';

class UserTable extends EntityTable<User> {
  const UserTable({super.key});

  @override
  State<EntityTable<User>> createState() {
    return UserTableState();
  }
}

class UserTableState extends EntityTableState<User, UserDataSource> {

  @override
  List<DataColumn> buildColumns() {
    return [
      buildColumn("Id"),
      buildColumn("First name"),
      buildColumn("Second name"),
      buildColumn("Third name")
    ];
  }

  @override
  SortableField? columnIndexToSortableField(int index) {
    return super.columnIndexToSortableField(index);
  }
}
