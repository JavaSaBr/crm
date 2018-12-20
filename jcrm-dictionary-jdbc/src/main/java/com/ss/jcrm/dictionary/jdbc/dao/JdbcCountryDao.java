package com.ss.jcrm.dictionary.jdbc.dao;

import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.jdbc.AbstractDictionaryDao;
import com.ss.jcrm.dictionary.jdbc.JdbcCountry;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class JdbcCountryDao extends AbstractDictionaryDao<Country> {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " FROM \"country\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " FROM \"country\" where \"id\" = ?";

    private static final String Q_SELECT_ALL = "select \"id\", \"name\", \"flag_code\", \"phone_code\" " +
        " FROM \"country\"";

    public JdbcCountryDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
    }

    @Override
    public @NotNull List<Country> findAll() {

        var result = new ArrayList<Country>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_ALL)
        ) {

            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(new JdbcCountry(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getLong(1)
                    ));
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return result;
    }

    @Override
    public @Nullable Country findById(long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_ID)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcCountry(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    @Override
    public @Nullable Country findByName(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_NAME)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcCountry(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }
}
