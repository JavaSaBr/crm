package com.ss.jcrm.dao;

import org.jetbrains.annotations.NotNull;

public interface NamedEntity extends Entity {

    @NotNull String getName();
}
