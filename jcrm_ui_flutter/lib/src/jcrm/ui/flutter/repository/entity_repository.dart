import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/entity/entity.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/repository/repository.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/resource/entity_resource.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/http_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/security_service.dart';

abstract class EntityRepository<E extends Entity, R extends EntityResource> extends Repository<E, R> {

  final HttpService _httpService;
  final SecurityService _securityService;

  EntityRepository(this._httpService, this._securityService);

  String buildFindByIdUrl(int id);

  Future<E> findById(int id) async {
    return _securityService
        .doGet(buildFindByIdUrl(id), (json) => readFromJson(json))
        .then((value) => convertFromResource(value));
  }

  R readFromJson(Map<String, dynamic> json);

  E convertFromResource(R resource);

  R convertToResource(E entity);
}