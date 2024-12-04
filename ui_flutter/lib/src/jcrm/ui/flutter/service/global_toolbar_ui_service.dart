
import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/service/ui_based_service.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/global_toolbar.dart';

class GlobalToolbarUiService extends UiBasedService {

  GlobalToolbar _toolbar = GlobalToolbar();

  Widget get toolbar => _toolbar;

  void changeTitleTo(String newTitle) {
    //_toolbar.
  }
}

