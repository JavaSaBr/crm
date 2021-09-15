import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';

class UniqEntityResource extends EntityResource {
  final int id;

  UniqEntityResource(this.id);

  UniqEntityResource.fromJson(Map<String, dynamic> json) : id = json['id'];

  @override
  void buildJson(Map<String, dynamic> json) {
    json['id'] = id;
  }
}
