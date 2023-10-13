import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/uniq_entity_table_data_source.dart';

class UserTableDataSource extends UniqEntityTableDataSource<User, UserResource, UserRepository> {
  
  UserTableDataSource(UserRepository repository) : super(repository);

  @override
  DataRow buildRow(User user) {
    return DataRow(
      key: ValueKey<int>(user.id),
      cells: [
        DataCell(Text(
          user.id.toString(),
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          user.firstName,
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          user.secondName,
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          user.thirdName,
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          user.email,
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          user.birthday == null ? "" : user.birthday!.toIso8601String(),
          textAlign: TextAlign.left,
        ))
      ],
    );
  }
}
