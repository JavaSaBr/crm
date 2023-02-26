import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';

class EntityResource extends JsonResource {

  EntityResource.fromEntity(Entity entity);

  EntityResource();

  @override
  void buildJson(Map<String, dynamic> json) {}

}
