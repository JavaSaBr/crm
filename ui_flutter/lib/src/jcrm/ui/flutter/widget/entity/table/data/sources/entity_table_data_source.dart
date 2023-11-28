import 'package:data_table_2/data_table_2.dart';
import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/entity_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/async_table_data_source.dart';

abstract class EntityTableDataSource<E extends Entity, R extends EntityResource,
    ER extends EntityRepository<E, R>> extends AsyncTableDataSource<E> {

  final ER repository;

  EntityTableDataSource(this.repository) {
    print('EntityDataSource created');
  }

  @override
  Future<AsyncRowsResponse> getRows(int offset, int end) async {
    print('getRows($offset, $end)');

    var pageSize = end - offset;
    var fetchPage = await repository.fetchPage(
        offset,
        pageSize,
        sortedField: sortField,
        direction: sortDirection);

    return AsyncRowsResponse(
        fetchPage.totalSize,
        fetchPage.entities
            .map((entity) => buildRow(entity))
            .toList(growable: false));
  }

  DataRow buildRow(E entity) {
    return DataRow(
      onSelectChanged: (value) {
        if (value != null) {
          setRowSelection(buildKey(entity), value);
        }
      },
      key: buildKey(entity),
      cells: buildRowCells(entity),
    );
  }

  @protected
  LocalKey buildKey(E entity);

  @protected
  List<DataCell> buildRowCells(E entity);
}
