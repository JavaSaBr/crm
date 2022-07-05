abstract class JsonResource {

  static List<T> fromJsonList<T extends JsonResource>(
      List<dynamic> json,
      T Function(Map<String, dynamic> json) readJson
  ) {
    return json.isEmpty ? [] : json
        .map((element) => readJson(element))
        .toList();
  }

  static List<dynamic> toJsonList(List<JsonResource> resources) {
    return resources.isEmpty ? [] : resources
        .map((element) => element.toJson())
        .toList();
  }

  Map<String, dynamic> toJson() {
    Map<String, dynamic> json = {};
    buildJson(json);
    return json;
  }

  void buildJson(Map<String, dynamic> json);
}
