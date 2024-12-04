import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/controller/base_widget_controller.dart';

class GlobalToolbar extends StatefulWidget {

  GlobalToolbar({super.key});

  @override
  _GlobalToolbarState createState() {
    return _GlobalToolbarState();
  }
}

class _GlobalToolbarState extends State<GlobalToolbar> {

  String title = "No title";

  @override
  Widget build(BuildContext context) {
    return Text(title);
  }
}

class GlobalToolbarController
    extends BaseWidgetController<GlobalToolbar, _GlobalToolbarState> {

  /// Show rows from the next page
  void goToNextPage() {
    _assertIfNotAttached();
    if (_state != null) {
      if (_state!._isNextPageUnavailable()) return;
      _state!._handleNext();
    }
  }
}