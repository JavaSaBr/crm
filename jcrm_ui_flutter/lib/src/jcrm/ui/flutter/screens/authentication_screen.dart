import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/routing.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/theme/theme_constants.dart';

class Credentials {
  final String username;
  final String password;

  Credentials(this.username, this.password);
}

class AuthenticationScreen extends StatefulWidget {
  final ValueChanged<Credentials> credentials;

  const AuthenticationScreen({
    required this.credentials,
    Key? key,
  }) : super(key: key);

  @override
  AuthenticationScreenState createState() => AuthenticationScreenState();
}

class AuthenticationScreenState extends State<AuthenticationScreen> {

  static final formSize = BoxConstraints.loose(const Size(600, 600));

  final _userController = TextEditingController();
  final _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) => Scaffold(
        body: Center(
            child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Card(
              margin: ThemeConstants.cardColumnOffset,
              child: Container(
                constraints: formSize,
                padding: ThemeConstants.cardPadding,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text('Sign in', style: Theme.of(context).textTheme.headline4),
                    Padding(
                        padding: ThemeConstants.cardFieldOffset,
                        child: TextField(
                          decoration: const InputDecoration(labelText: 'Username'),
                          controller: _userController,
                        )),
                    Padding(
                        padding: ThemeConstants.cardFieldOffset,
                        child: TextField(
                          decoration: const InputDecoration(labelText: 'Password'),
                          obscureText: true,
                          controller: _passwordController,
                        )),
                    Padding(
                      padding: ThemeConstants.cardFieldOffset,
                      child: ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            minimumSize: ThemeConstants.cardButtonSize,
                          ),
                          onPressed: () async {
                            widget.credentials(Credentials(_userController.value.text, _passwordController.value.text));
                          },
                          child: const Text('Sign in')),
                    ),
                    Container(
                      alignment: Alignment.centerLeft,
                      child: TextButton(
                          onPressed: () async {
                            RouteStateScope.of(context).go('/reset-password');
                          },
                          child: const Text('Forgot password?')),
                    )
                  ],
                ),
              ),
            ),
            Card(
              margin: ThemeConstants.cardColumnOffset,
              child: Container(
                constraints: formSize,
                padding: ThemeConstants.cardPadding,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text("Do you not have an account?"),
                    TextButton(
                        onPressed: () async {
                          RouteStateScope.of(context).go('/register');
                        },
                        child: const Text('Register')),
                  ],
                ),
              ),
            )
          ],
        )),
      );
}
