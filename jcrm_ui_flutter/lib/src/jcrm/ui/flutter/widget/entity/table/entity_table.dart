import 'package:data_table_2/paginated_data_table_2.dart';
import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';

abstract class EntityTable<T extends Entity> extends StatefulWidget {
  final EntityDataSource<T> dataSource;

  const EntityTable({Key? key, required this.dataSource}) : super(key: key);
}

abstract class EntityTableState<T extends Entity> extends State<EntityTable> with SingleTickerProviderStateMixin {
  static const headerTextStyle = TextStyle(fontWeight: FontWeight.bold);

  static DataColumn buildColumn(String label) {
    return DataColumn(label: Text(label, style: headerTextStyle));
  }

  int _rowsPerPage = PaginatedDataTable.defaultRowsPerPage;
  bool _sortAscending = true;
  int? _sortColumnIndex;
  bool _initialized = false;

  final EntityDataSource<T> _dataSource;

  EntityTableState(this._dataSource);

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(4),
      child: PaginatedDataTable2(
        checkboxHorizontalMargin: 12,
        autoRowsToHeight: true,
        columnSpacing: 0,
        fit: FlexFit.tight,
        columns: buildColumns(),
        source: _dataSource,
        wrapInCard: false,
        rowsPerPage: _rowsPerPage,
        onRowsPerPageChanged: (value) {
          // No need to wrap into setState, it will be called inside the widget
          // and trigger rebuild
          //setState(() {
          _rowsPerPage = value!;
          print(_rowsPerPage);
          //});
        },
        initialFirstRowIndex: 0,
        empty: Center(
            child: Container(padding: const EdgeInsets.all(20), color: Colors.grey[200], child: const Text('No data'))),
        sortColumnIndex: _sortColumnIndex,
        sortAscending: _sortAscending,
      ),
    );
  }

  get restorationPrefix => "entity_table_list_view";

  List<DataColumn> buildColumns() {
    return [buildColumn("Id")];
  }
}

class EntityDataSource<T extends Entity> extends DataTableSource {
  List<T> _entities = [];

  EntityDataSource(this._entities);

  @override
  DataRow? getRow(int index) {
    if (index >= _entities.length) {
      return null;
    }

    return DataRow.byIndex(index: index, cells: [
      DataCell(Text(
        _entities[index].toString(),
        textAlign: TextAlign.left,
      ))
    ]);
  }

  @override
  bool get isRowCountApproximate => false;

  @override
  int get rowCount => _entities.length;

  @override
  int get selectedRowCount => 0;

  List<T> get entities => _entities;
}