import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity_page.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sort_direction.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/sortable_field.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_page_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

abstract class EntityRepository<E extends Entity, R extends EntityResource> extends Repository<E, R> {

  final HttpService _httpService;
  final SecurityService _securityService;

  EntityRepository(this._httpService, this._securityService);

  Future<E> findById(int id) async {
    return _securityService
        .doGet(buildFetchByIdUrl(id), (json) => readFromJson(json))
        .then((value) => convertFromResource(value));
  }

  Future<EntityPage<E>> fetchPage(
      int offset,
      int pageSize, {
        SortableField? sortedField,
        SortDirection direction = SortDirection.asc
      }) async {
    return _securityService
        .doGet(
            buildFetchPageUrl(offset, pageSize),
            (json) => EntityPageResource.fromJson(json, (json) => readFromJson(json)))
        .then((resource) => EntityPage(
            resource.resources
                .map((element) => convertFromResource(element))
                .toList(growable: false),
            resource.totalSize));
  }

  R readFromJson(Map<String, dynamic> json);

  E convertFromResource(R resource);

  R convertToResource(E entity);

  String fetchByIdUrl();

  String buildFetchByIdUrl(int id) {
    return "${fetchByIdUrl()}/$id";
  }

  String fetchPageUrl();

  String buildFetchPageUrl(
      int offset,
      int pageSize, {
        SortableField? sortedField,
        SortDirection direction = SortDirection.asc
      }) {

    var url = "${fetchPageUrl()}?offset=$offset&pageSize=$pageSize";

    if (sortedField != null) {
      return "$url&sortField=${sortedField.fieldIndex()}&sortDirection=$direction";
    }

    return url;
  }
}