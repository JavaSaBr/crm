import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';

enum UserSortableField implements SortableField {
  name(2);

  final int _fieldIndex;

  const UserSortableField(this._fieldIndex);

  @override
  int fieldIndex() {
    return _fieldIndex;
  }
}