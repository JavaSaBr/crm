abstract class JsonResource {
  Map<String, dynamic> toJson() {
    Map<String, dynamic> json = {};
    buildJson(json);
    return json;
  }

  void buildJson(Map<String, dynamic> json);
}
