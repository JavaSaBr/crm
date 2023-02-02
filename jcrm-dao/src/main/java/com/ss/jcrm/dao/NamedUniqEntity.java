package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;

public interface NamedUniqEntity extends UniqEntity {
  @NotNull String name();
}
