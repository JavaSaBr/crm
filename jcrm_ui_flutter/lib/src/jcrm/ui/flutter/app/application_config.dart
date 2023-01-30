import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'dart:html';

import '../services.dart';

class ApplicationConfig extends MultiProvider {
  ApplicationConfig(TransitionBuilder appBuilder, {Key? appKey})
      : super(
            key: appKey,
            providers: [
              Provider(create: (context) => ErrorService())
            ],
            child: MultiProvider(
                providers: [
                  Provider(create: (context) => HttpService(context.read<ErrorService>()))
                ],
                child: MultiProvider(
                  providers: [
                    Provider(create: (context) => RegistrationService(context.read<HttpService>()))
                  ],
                  child: MultiProvider(
                      providers: [
                        ChangeNotifierProvider(create: (context) => SecurityService(context.read<HttpService>(), window.localStorage)),
                      ],
                      builder: appBuilder),
                )));
}
