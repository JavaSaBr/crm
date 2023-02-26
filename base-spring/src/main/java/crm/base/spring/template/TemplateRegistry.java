package crm.base.spring.template;

import crm.base.spring.util.ResourceUtils;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class TemplateRegistry {

  @NotNull String template;

  public TemplateRegistry(@NotNull String basePath) {
    this.template = ResourceUtils.readAsString(basePath);
  }
}
