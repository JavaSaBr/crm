package crm.integration.test.model;

import java.util.Map;

@FunctionalInterface
public interface PropertiesTestProvider {
  Map<String, Object> provide();
}
