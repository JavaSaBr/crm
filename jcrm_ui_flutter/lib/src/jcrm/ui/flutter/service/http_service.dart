import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/json_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/error_service.dart';

class HttpService {

  final ErrorService errorService;

  HttpService(this.errorService);

  Future<T> post<T extends JsonResource>(
      String url,
      JsonResource body,
      T Function(Map<String, dynamic> json) readJson
  ) async {
    var client = http.Client();
    try {
      var uri = Uri.parse(url);
      var response = await client.post(uri, body: body.toJson());

      errorService.throwHttpErrorIfNeed(response);

      var decodedResponse = jsonDecode(utf8.decode(response.bodyBytes)) as Map<String, dynamic>;
      var resource = readJson(decodedResponse);

      return resource;
    } finally {
      client.close();
    }
  }

  Future<T> get<T extends JsonResource>(
      String url,
      T Function(Map<String, dynamic> json) jsonReader,
      Map<String, String>? headers
  ) async {
    var client = http.Client();
    try {
      var uri = Uri.parse(url);
      var response = await client.get(uri, headers: headers);

      errorService.throwHttpErrorIfNeed(response);

      var decodedResponse = jsonDecode(utf8.decode(response.bodyBytes)) as Map<String, dynamic>;
      var resource = jsonReader(decodedResponse);

      return resource;
    } finally {
      client.close();
    }
  }
}