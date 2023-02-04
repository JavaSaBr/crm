package com.ss.jcrm.dao;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public record EntityPage<T extends Entity>(@NotNull List<T> entities, long totalSize) {}
