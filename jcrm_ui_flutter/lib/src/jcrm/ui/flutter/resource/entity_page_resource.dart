import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/util/resources_utils.dart';

class EntityPageResource<R extends JsonResource> extends JsonResource {
  final int totalSize;
  final List<R> resources;

  EntityPageResource(this.totalSize, this.resources);

  EntityPageResource.fromJson(
      Map<String, dynamic> json,
      R Function(Map<String, dynamic> json) jsonReader)
      : totalSize = json['totalSize'],
        resources = ResourcesUtils.asJsonArray(json['resources'])
            .map((subJson) => jsonReader(subJson))
            .toList(growable: false);

  @override
  void buildJson(Map<String, dynamic> json) {
    json['totalSize'] = totalSize;
    json['resources'] = resources
        .map((subResource) => subResource.toJson())
        .toList(growable: false);
  }
}
