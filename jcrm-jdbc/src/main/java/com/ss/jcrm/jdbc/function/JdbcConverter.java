package com.ss.jcrm.jdbc.function;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.Entity;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcConverter<T extends Entity, D extends Dao<T>> {

    @NotNull T convert(@NotNull D dao, @NotNull ResultSet rset) throws SQLException;
}
