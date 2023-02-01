import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/uniq_entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/entity_repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/entity/table/data/sources/entity_table_data_source.dart';

abstract class UniqEntityTableDataSource<
    E extends UniqEntity,
    R extends EntityResource,
    ER extends EntityRepository<E, R>> extends EntityTableDataSource<E, R, ER> {

  UniqEntityTableDataSource(ER repository) : super(repository);
}
