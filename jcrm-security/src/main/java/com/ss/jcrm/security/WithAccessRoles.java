package com.ss.jcrm.security;

import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface WithAccessRoles {

  @NotNull Set<AccessRole> roles();

  default @NotNull Stream<AccessRole> streamRoles() {
    return roles().stream();
  }
}
