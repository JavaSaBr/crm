import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/uniq_entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';

class UniqEntityResource extends EntityResource {
  final int id;

  UniqEntityResource.fromEntity(UniqEntity entity) : id = entity.id;

  UniqEntityResource.fromJson(Map<String, dynamic> json) : id = json['id'];

  UniqEntityResource(this.id);

  @override
  void buildJson(Map<String, dynamic> json) {
    json['id'] = id;
  }
}
