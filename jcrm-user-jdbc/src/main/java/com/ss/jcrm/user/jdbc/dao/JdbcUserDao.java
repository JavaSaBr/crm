package com.ss.jcrm.user.jdbc.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toSet;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.ss.jcrm.jdbc.dao.AbstractJdbcDao;
import com.ss.jcrm.jdbc.exception.CannotGetGeneratedKeysJdbcException;
import com.ss.jcrm.jdbc.exception.NotRelevantVersionJdbcException;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.UserRole;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserRoleDao;
import com.ss.jcrm.user.jdbc.JdbcUser;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.postgresql.util.PGobject;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

@Log4j2
public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

    private static final String Q_SELECT_BY_NAME = "select \"id\", \"name\", \"password\", \"slat\", " +
        "\"organization\", \"roles\", \"version\" FROM \"user\" where \"name\" = ?";

    private static final String Q_SELECT_BY_ID = "select \"name\", \"password\", \"slat\", \"organization\", " +
        "\"roles\", \"version\" FROM \"user\" where \"id\" = ?";

    private static final String Q_INSERT = "INSERT INTO \"user\" (\"id\", \"name\", \"password\", " +
        "\"salt\", \"organization\") VALUES (DEFAULT, ?,?,?,?)";

    private static final String Q_UPDATE_ROLES = "UPDATE \"user\" SET \"roles\" = ?, \"version\" = ? " +
        " WHERE \"id\" = ? and \"version\" = ?";

    private final OrganizationDao organizationDao;
    private final UserRoleDao userRoleDao;

    public JdbcUserDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor,
        @NotNull OrganizationDao organizationDao,
        @NotNull UserRoleDao userRoleDao
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
        this.organizationDao = organizationDao;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public @NotNull User create(
        @NotNull String name,
        @NotNull String password,
        @NotNull byte[] salt,
        @Nullable Organization organization
    ) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, name);
            statement.setString(2, password);
            statement.setBytes(3, salt);
            statement.setLong(4, organization == null ? 0L : organization.getId());
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcUser(
                        name,
                        password,
                        salt,
                        organization,
                        Collections.emptySet(),
                        rs.getLong(1),
                        0
                    );
                } else {
                    throw new CannotGetGeneratedKeysJdbcException("Can't receive generated id for the new user entity.");
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull User> createAsync(
        @NotNull String name,
        @NotNull String password,
        @NotNull byte[] salt,
        @Nullable Organization organization
    ) {
        return supplyAsync(() -> create(name, password, salt, organization), fastDbTaskExecutor);
    }

    @Override
    public @Nullable User findByName(@NotNull String name) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_NAME)
        ) {

            statement.setString(1, name);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcUser(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        organizationDao.findById(rs.getLong(5)),
                        parseRoles(rs.getString(6)),
                        rs.getLong(1),
                        rs.getInt(7)
                    );
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable User> findByNameAsync(@NotNull String name) {
        return supplyAsync(() -> findByName(name), fastDbTaskExecutor);
    }

    @Override
    public @Nullable User findById(long id) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_SELECT_BY_ID)
        ) {

            statement.setLong(1, id);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new JdbcUser(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        organizationDao.findById(rs.getLong(5)),
                        parseRoles(rs.getString(6)),
                        id,
                        rs.getInt(7)
                    );
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }

        return null;
    }

    @Override
    public @NotNull CompletableFuture<@Nullable User> findByIdAsync(long id) {
        return supplyAsync(() -> findById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull User requireById(long id) {
        return notNull(findById(id));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull User> requireByIdAsync(long id) {
        return supplyAsync(() -> requireById(id), fastDbTaskExecutor);
    }

    @Override
    public @NotNull User addRole(@NotNull User user, @NotNull UserRole role) {

        var roles = user.getRoles();

        if (roles.contains(role)) {
            log.warn("the user {} already has the role {}.", user.getName(), role.getName());
            return user;
        }

        var newRoles = new HashSet<UserRole>(roles);
        newRoles.add(role);

        return updateRoles(user, newRoles);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull User> addRoleAsync(
        @NotNull User user,
        @NotNull UserRole role
    ) {
        return supplyAsync(() -> addRole(user, role), fastDbTaskExecutor);
    }

    @Override
    public @NotNull User removeRole(
        @NotNull User user,
        @NotNull UserRole role
    ) {

        var roles = user.getRoles();

        if (!roles.contains(role)) {
            log.warn("the user {} doesn't have the role {}.", user.getName(), role.getName());
            return user;
        }

        var newRoles = Collections.<UserRole>emptySet();

        if (roles.size() > 1) {
            newRoles = new HashSet<>(roles);
            newRoles.remove(role);
        }

        return updateRoles(user, newRoles);
    }


    private @NotNull User updateRoles(@NotNull User user, @NotNull Set<UserRole> newRoles) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_UPDATE_ROLES)
        ) {

            int version = user.getVersion();

            statement.setObject(1, rolesToJson(newRoles));
            statement.setInt(2, version + 1);
            statement.setLong(3, user.getId());
            statement.setInt(4, version);

            if (statement.executeUpdate() != 1) {
                throw new NotRelevantVersionJdbcException("The user's version " + version + " is outdated.");
            }

            user.setVersion(version + 1);
            user.setRoles(newRoles);

            return user;

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull User> removeRoleAsync(
        @NotNull User user, @NotNull UserRole role
    ) {
        return supplyAsync(() -> removeRole(user, role), fastDbTaskExecutor);
    }

    private @NotNull Set<UserRole> parseRoles(@Nullable String json) {

        if (StringUtils.isEmpty(json)) {
            return Collections.emptySet();
        }

        var deserialize = JsonIterator.deserialize(json, int[].class);

        if (deserialize == null || deserialize.length < 1) {
            return Collections.emptySet();
        }

        return IntStream.of(deserialize)
            .mapToObj(userRoleDao::findById)
            .filter(Objects::nonNull)
            .collect(toSet());
    }

    private @Nullable PGobject rolesToJson(@NotNull Set<UserRole> roles) throws SQLException {

        if (roles.isEmpty()) {
            return null;
        }

        var result = new PGobject();
        result.setType("json");
        result.setValue(JsonStream.serialize(roles.stream()
            .mapToLong(UserRole::getId)
            .toArray()));

        return result;
    }
}
