import 'package:data_table_2/data_table_2.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sort_direction.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';

abstract class AsyncTableDataSource<E> extends AsyncDataTableSource {

  SortableField? _sortField = null;
  SortDirection _sortDirection = SortDirection.asc;

  SortDirection get sortDirection => _sortDirection;
  SortableField? get sortField => _sortField;

  void changeSorting(SortableField? sortColumn, SortDirection sortDirection) {
    _sortField = sortColumn;
    _sortDirection = sortDirection;
    refreshDatasource();
  }

}