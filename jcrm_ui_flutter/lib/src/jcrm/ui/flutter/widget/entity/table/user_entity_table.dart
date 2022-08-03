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
              User(3, "test@test", "Vlad", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(4, "test@test", "Anton", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),

              User(5, "test@test", "Alex", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(6, "test@test", "Olga", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(7, "test@test", "Vlad", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(8, "test@test", "Anton", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),

              User(9, "test@test", "Alex", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(10, "test@test", "Olga", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(11, "test@test", "Vlad", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(12, "test@test", "Anton", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),

              User(13, "test@test", "Alex", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(14, "test@test", "Olga", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(15, "test@test", "Vlad", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
              User(16, "test@test", "Anton", "Second name", "Third name", DateTime.now(), [], [], DateTime.now(),
                  DateTime.now()),
            ]));

  @override
  State<StatefulWidget> createState() => UserTableState(dataSource);
}

class UserTableState extends EntityTableState<User> {
  UserTableState(EntityDataSource<User> dataSource) : super(dataSource);

  @override
  List<DataColumn> buildColumns() {
    return [
      EntityTableState.buildColumn("Id"),
      EntityTableState.buildColumn("First name"),
      EntityTableState.buildColumn("Second name"),
      EntityTableState.buildColumn("Third name")
    ];
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
      )),
      DataCell(Text(
        entities[index].secondName,
        textAlign: TextAlign.left,
      )),
      DataCell(Text(
        entities[index].thirdName,
        textAlign: TextAlign.left,
      ))
    ]);
  }
}