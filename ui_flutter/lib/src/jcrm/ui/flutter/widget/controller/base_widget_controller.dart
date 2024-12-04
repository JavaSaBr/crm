import 'package:flutter/material.dart';

class BaseWidgetController<W extends StatefulWidget, S extends State<W>>
    extends ChangeNotifier {
  S? _state;

  bool get isAttached => _state != null;

  void attach(S state) {
    _state = state;
  }

  void detach() {
    _state = null;
  }

  @protected
  void notifyListeners() {
    notifyListeners();
  }

  @protected
  void checkAttachedAndThrow() {
    if (_state == null) {
      throw 'Controller is not attached to any Widget and can\'t be used';
    }
  }

  @protected
  void assertIfNotAttached() {
    assert(_state != null,
        'Controller is not attached to any Widget and can\'t be used');
  }
}
