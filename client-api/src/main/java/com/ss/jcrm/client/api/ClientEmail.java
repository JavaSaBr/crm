package com.ss.jcrm.client.api;

import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;

public interface ClientEmail extends Entity {
  @NotNull String email();
  @NotNull EmailType type();
}
