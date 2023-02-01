import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/user.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/user_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/user_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/uniq_entity_data_source.dart';

class UserDataSource extends UniqEntityDataSource<User, UserResource, UserRepository> {
  
  UserDataSource(UserRepository repository) : super(repository);

  @override
  DataRow buildRow(User entity) {
    return DataRow(
      key: ValueKey<int>(entity.id),
      cells: [
        DataCell(Text(
          entity.id.toString(),
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          entity.firstName,
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          entity.secondName,
          textAlign: TextAlign.left,
        )),
        DataCell(Text(
          entity.thirdName,
          textAlign: TextAlign.left,
        ))
      ],
    );
  }
}
