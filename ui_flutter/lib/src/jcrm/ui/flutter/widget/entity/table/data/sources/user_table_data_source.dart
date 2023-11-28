import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/uniq_entity_table_data_source.dart';

class UserTableDataSource extends UniqEntityTableDataSource<User, UserResource, UserRepository> {
  
  UserTableDataSource(UserRepository repository) : super(repository);

  @override
  LocalKey buildKey(User entity) {
    return ValueKey<int>(entity.id);
  }

  @override
  List<DataCell> buildRowCells(User entity) {
    return [
      DataCell(Text(
        entity.id.toString(),
        textAlign: TextAlign.left,
      )),
      DataCell(Text(
        buildName(entity),
        textAlign: TextAlign.left,
      )),
      DataCell(Text(
        entity.email,
        textAlign: TextAlign.left,
      )),
      DataCell(Text(
        entity.birthday == null ? "" : entity.birthday!.toIso8601String(),
        textAlign: TextAlign.left,
      ))
    ];
  }

  String buildName(User entity) {
    return entity.names().join(" ");
  }
}
