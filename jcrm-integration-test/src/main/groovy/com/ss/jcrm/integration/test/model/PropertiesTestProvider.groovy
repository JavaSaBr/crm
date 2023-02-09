package com.ss.jcrm.integration.test.model

@FunctionalInterface
interface PropertiesTestProvider {
  Map<String, String> provide();
}