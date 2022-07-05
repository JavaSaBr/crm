import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/entity_table.dart';

class UserTable extends EntityTable<User> {
  UserTable({Key? key})
      : super(
            key: key,
            dataSource: UserDataSource([
              User(1, "test@test", "Alex", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(2, "test@test", "Olga", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(2, "test@test", "Vlad", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(2, "test@test", "Anton", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
            ]));

  @override
  State<StatefulWidget> createState() => UserTableState(dataSource);
}

class UserTableState extends EntityTableState<User> {
  UserTableState(EntityDataSource<User> dataSource) : super(dataSource);

  @override
  List<DataColumn> buildColumns() {
    return [const DataColumn(label: Text("Id"), numeric: true), const DataColumn(label: Text("First name"))];
  }
}

class UserDataSource extends EntityDataSource<User> {
  UserDataSource(List<User> users) : super(users);

  @override
  DataRow? getRow(int index) {
    if (index >= entities.length) {
      return null;
    }

    return DataRow.byIndex(index: index, cells: [
      DataCell(Text(
        entities[index].id.toString(),
        textAlign: TextAlign.left,
      )),
      DataCell(Text(
        entities[index].firstName,
        textAlign: TextAlign.left,
      ))
    ]);
  }
}