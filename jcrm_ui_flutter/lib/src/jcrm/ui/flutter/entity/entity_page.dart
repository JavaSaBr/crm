import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';

class EntityPage<E extends Entity> {

  List<E> entities;
  int totalSize;

  EntityPage(this.entities, this.totalSize);
}