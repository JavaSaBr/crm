import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';

abstract class EntityTable<T extends Entity> extends StatefulWidget {

  final EntityDataSource<T> dataSource;

  const EntityTable({Key? key, required this.dataSource}) : super(key: key);
}

abstract class EntityTableState<T extends Entity> extends State<EntityTable> with SingleTickerProviderStateMixin {

  final EntityDataSource<T> _dataSource;

  EntityTableState(this._dataSource);

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      restorationId: restorationPrefix,
      padding: const EdgeInsets.all(16),
      children: [
        PaginatedDataTable(columns: buildColumns(), source: _dataSource)
      ],
    );
  }

  get restorationPrefix => "entity_table_list_view";

  List<DataColumn> buildColumns() {
    return [
      const DataColumn(
        label: Text("Id"),
        numeric: true
      )
    ];
  }
}

class EntityDataSource<T extends Entity> extends DataTableSource {

  List<T> _etities = [];
  
  EntityDataSource(this._etities);

  @override
  DataRow? getRow(int index) {

    if(index >= _etities.length) {
      return null;
    }

    return DataRow.byIndex(
        index: index,
        cells: [
          DataCell(Text(
              _etities[index].toString(),
            textAlign: TextAlign.left,
          ))
        ]
    );
  }

  @override
  bool get isRowCountApproximate => false;

  @override
  int get rowCount => _etities.length;

  @override
  int get selectedRowCount => 0;
}