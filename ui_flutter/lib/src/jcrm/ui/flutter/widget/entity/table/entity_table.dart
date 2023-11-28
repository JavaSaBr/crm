import 'package:flutter/material.dart';
import 'package:data_table_2/data_table_2.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sort_direction.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/async_table_data_source.dart';

abstract class EntityTable<T extends Entity> extends StatefulWidget {
  const EntityTable({super.key});

  @override
  State<EntityTable<T>> createState();
}

abstract class EntityTableState<T extends Entity,
    D extends AsyncTableDataSource<T>> extends State<EntityTable<T>> {
  static const headerTextStyle = TextStyle(fontWeight: FontWeight.bold);

  final PaginatorController _controller = PaginatorController();

  int _rowsPerPage = PaginatedDataTable.defaultRowsPerPage;

  bool _sortAscending = true;
  int? _sortColumnIndex;
  bool _dataSourceLoading = false;

  late final D _dataSource;

  @override
  void initState() {
    super.initState();
    _dataSource = createDataSource();
  }

  @protected
  D createDataSource();

  // Use global key to avoid rebuilding state of _TitledRangeSelector
  // upon AsyncPaginatedDataTable2 refreshes, e.g. upon page switches
  //final GlobalKey _rangeSelectorKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    // Last page example uses extra API call to get the number of items in datasource
    if (_dataSourceLoading) return const SizedBox();

    return Container(
      padding: const EdgeInsets.all(4),
      child: AsyncPaginatedDataTable2(
          checkboxHorizontalMargin: 12,
          onSelectAll: (select) => select != null && select
              ? _dataSource.selectAllOnThePage()
              : _dataSource.deselectAllOnThePage(),
          sortColumnIndex: _sortColumnIndex,
          sortAscending: _sortAscending,
          sortArrowIcon: Icons.keyboard_arrow_up,
          sortArrowAnimationDuration: const Duration(milliseconds: 0),
          horizontalMargin: 20,
          columnSpacing: 0,
          wrapInCard: false,
          rowsPerPage: _rowsPerPage,
          fit: FlexFit.tight,
          columns: buildColumns(),
          autoRowsToHeight: true,
          pageSyncApproach: PageSyncApproach.doNothing,
          minWidth: 800,
          onRowsPerPageChanged: (value) {
            // No need to wrap into setState, it will be called inside the widget
            // and trigger rebuild
            //setState(() {
            print('Row per page changed to $value');
            _rowsPerPage = value!;
            //});
          },
          controller: _controller,
          hidePaginator: true,
          empty: Center(
              child: Container(
                  padding: const EdgeInsets.all(20),
                  color: Colors.grey[200],
                  child: const Text('No data'))),
          loading: _Loading(),
          errorBuilder: (e) => _ErrorAndRetry(
              e.toString(), () => _dataSource.refreshDatasource()),
          source: _dataSource),
    );
  }

  get restorationPrefix => "entity_table_list_view";

  @protected
  SortableField? columnIndexToSortableField(int index) {
    return null;
  }

  void sort(int columnIndex, bool ascending) {
    _dataSource.changeSorting(columnIndexToSortableField(columnIndex),
        SortDirection.forAscending(ascending));

    setState(() {
      _sortColumnIndex = columnIndex;
      _sortAscending = ascending;
    });
  }

  @protected
  List<DataColumn> buildColumns() {
    return [buildColumn("Id")];
  }

  @protected
  DataColumn buildColumn(String label) {
    return DataColumn2(
      label: Text(label, style: headerTextStyle),
      size: ColumnSize.L,
      onSort: (columnIndex, ascending) => sort(columnIndex, ascending),
    );
  }

  @protected
  DataColumn buildShortColumn(String label) {
    return DataColumn2(
      label: Text(label, style: headerTextStyle),
      size: ColumnSize.S,
      onSort: (columnIndex, ascending) => sort(columnIndex, ascending),
    );
  }
}

class _ErrorAndRetry extends StatelessWidget {
  const _ErrorAndRetry(this.errorMessage, this.retry);

  final String errorMessage;
  final void Function() retry;

  @override
  Widget build(BuildContext context) => Center(
        child: Container(
            padding: const EdgeInsets.all(10),
            height: 70,
            color: Colors.red,
            child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('Oops! $errorMessage',
                      style: const TextStyle(color: Colors.white)),
                  TextButton(
                      onPressed: retry,
                      child:
                          Row(mainAxisSize: MainAxisSize.min, children: const [
                        Icon(
                          Icons.refresh,
                          color: Colors.white,
                        ),
                        Text('Retry', style: TextStyle(color: Colors.white))
                      ]))
                ])),
      );
}

class _Loading extends StatefulWidget {
  @override
  __LoadingState createState() => __LoadingState();
}

class __LoadingState extends State<_Loading> {
  @override
  Widget build(BuildContext context) {
    return ColoredBox(
        color: Colors.white.withAlpha(128),
        // at first show shade, if loading takes longer than 0,5s show spinner
        child: FutureBuilder(
            future:
                Future.delayed(const Duration(milliseconds: 500), () => true),
            builder: (context, snapshot) {
              return !snapshot.hasData
                  ? const SizedBox()
                  : Center(
                      child: Container(
                      color: Colors.yellow,
                      padding: const EdgeInsets.all(7),
                      width: 150,
                      height: 50,
                      child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: const [
                            CircularProgressIndicator(
                              strokeWidth: 2,
                              color: Colors.black,
                            ),
                            Text('Loading..')
                          ]),
                    ));
            }));
  }
}
