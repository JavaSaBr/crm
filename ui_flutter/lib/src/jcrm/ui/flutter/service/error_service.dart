import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/exception/http_exception.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/model/http_error.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/util/value_wrapper.dart';

class ErrorService {

  void throwHttpErrorIfNeed(Response response) {
    if (response.statusCode < 400) {
      return;
    }

    var bodyBytes = response.bodyBytes;

    if (bodyBytes.isNotEmpty) {
      var decoded = jsonDecode(utf8.decode(bodyBytes)) as Map<String, dynamic>;
      throwHttpError(HttpError.fromJson(decoded));
    } else {
      throwHttpError(HttpError(response.statusCode, response.reasonPhrase!));
    }
  }

  void throwHttpError(HttpError httpError) {
    throw HttpException(httpError);
  }

  void handleErrorOnUi(BuildContext context, Exception exception) {

    if (kDebugMode) {
      print(exception);
    }

    var value = ValueWrapper<ScaffoldFeatureController<SnackBar, SnackBarClosedReason>>(null);

    var messenger = ScaffoldMessenger.of(context);
    var controller = messenger.showSnackBar(SnackBar(
        content: Text(getErrorMessage(exception)),
        action: SnackBarAction(
            label: "Close",
            onPressed: () {
              value.value!.close();
            })));

    value.value = controller;
  }

  String getErrorMessage(Exception exception) {
   
    if (exception is HttpException) {
      return exception.error.errorMessage;
    }

    return exception.toString();
  }
}

class Errors {

  static const tokenExpired = 2000;
  static const tokenMaxRefreshed = 2001;
}