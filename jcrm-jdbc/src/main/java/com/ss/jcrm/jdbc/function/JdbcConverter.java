package com.ss.jcrm.jdbc.function;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcConverter<D extends Dao<T>, T extends Entity> {

    @Nullable T convert(@NotNull D dao, @NotNull ResultSet rset) throws SQLException;
}
