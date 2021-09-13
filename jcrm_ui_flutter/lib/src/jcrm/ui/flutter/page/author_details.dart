// Copyright 2021, the Flutter project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

import 'package:flutter/material.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/data/author.dart';
import 'package:jcrm_ui_flutter/src/jcrm/ui/flutter/widget/book_list.dart';

import '../routing.dart';

class AuthorDetailsScreen extends StatelessWidget {
  final Author author;

  const AuthorDetailsScreen({
    Key? key,
    required this.author,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text(author.name),
    ),
    body: Center(
      child: Column(
        children: [
          Expanded(
            child: BookList(
              books: author.books,
              onTap: (book) {
                RouteStateScope.of(context).go('/book/${book.id}');
              },
            ),
          ),
        ],
      ),
    ),
  );
}