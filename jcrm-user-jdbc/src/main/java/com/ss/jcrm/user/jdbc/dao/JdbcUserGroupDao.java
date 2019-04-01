package com.ss.jcrm.user.jdbc.dao;

import static java.util.Collections.unmodifiableSet;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.exception.DaoException;
import com.ss.jcrm.jdbc.dao.AbstractJdbcDao;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.UserGroup;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.jdbc.JdbcUserGroup;
import com.ss.rlib.common.util.dictionary.LongDictionary;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class JdbcUserGroupDao extends AbstractJdbcDao<UserGroup> implements UserGroupDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_ALL_BY_ORG_ID = "select \"id\", \"name\", \"organization_id\", \"roles\"," +
        " \"version\" from \"user_group\" where \"organization_id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"name\", \"organization_id\", \"roles\"," +
        " \"version\" from \"user_group\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"user_group\" (\"name\", \"organization_id\") values (?, ?)";

    private final OrganizationDao organizationDao;

    public JdbcUserGroupDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor,
        @NotNull OrganizationDao organizationDao
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
        this.organizationDao = organizationDao;
    }

    @Override
    public @NotNull UserGroup create(@NotNull String name, @NotNull Organization organization) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.setLong(2, organization.getId());
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcUserGroup(rs.getLong(1), name, organization);
                } else {
                    throw new IllegalStateException(
                        "Can't receive generated id for the new user group [" + name + "]."
                    );
                }
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull UserGroup> createAsync(
        @NotNull String name,
        @NotNull Organization organization
    ) {
        return supplyAsync(() -> create(name, organization), fastDbTaskExecutor);
    }

    @Override
    public @Nullable UserGroup findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcUserGroupDao::toUserGroup);
    }

    @Override
    public @NotNull Set<UserGroup> getAll(@NotNull Organization organization) {

        var result = findAllByLong(
            Q_SELECT_ALL_BY_ORG_ID,
            organization.getId(),
            JdbcUserGroupDao::toUserGroup,
            HashSet::new
        );

        return unmodifiableSet(result);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Set<UserGroup>> getAllAsync(@NotNull Organization organization) {
        return supplyAsync(() -> getAll(organization), slowDbTaskExecutor);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull LongDictionary<UserGroup>> getAllAsMapAsync(
        @NotNull Organization organization
    ) {
        return supplyAsync(() -> getAllAsMap(organization), slowDbTaskExecutor);
    }

    private @NotNull UserGroup toUserGroup(@NotNull ResultSet rs) throws SQLException {

        var organization = organizationDao.requireById(rs.getLong(3));
        var roles = JdbcUtils.fromJsonArray(
            rs.getString(4),
            AccessRole::require
        );

        return new JdbcUserGroup(
            rs.getLong(1),   // id
            rs.getString(2), // name
            organization,
            roles,
            rs.getInt(5)     // version
        );
    }
}
