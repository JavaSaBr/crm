import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/uniq_entity_resource.dart';

class UniqEntity extends Entity {
  static final UniqEntity none = UniqEntity(0);

  int id;

  UniqEntity(this.id);

  UniqEntity.fromResource(UniqEntityResource resource) : id = resource.id;
}
