import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';

enum UniqEntitySortableField implements SortableField {
  id(1);

  final int _fieldIndex;

  const UniqEntitySortableField(this._fieldIndex);

  @override
  int fieldIndex() {
    return _fieldIndex;
  }
}