package crm.base.spring.util;

import com.ss.rlib.common.util.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;

public class ResourceUtils {

  public static @NotNull String readAsString(@NotNull String classpath) {
    return IOUtils.toString(() -> new ClassPathResource(classpath).getInputStream());
  }
}
