package com.ss.jcrm.user.jdbc.dao;

import static com.ss.jcrm.jdbc.util.JdbcUtils.toJsonArray;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.exception.GenerateIdDaoException;
import com.ss.jcrm.dao.exception.NotActualObjectDaoException;
import com.ss.jcrm.jdbc.dao.AbstractJdbcDao;
import com.ss.jcrm.jdbc.util.JdbcUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.jdbc.JdbcUser;
import lombok.extern.log4j.Log4j2;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Log4j2
public class JdbcUserDao extends AbstractJdbcDao<User> implements UserDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_EMAIL = "select \"id\", \"organization_id\", \"email\", \"first_name\"," +
        " \"second_name\", \"third_name\", \"phone_number\", \"password\", \"salt\", \"roles\", \"groups\"," +
        " \"version\", \"email_confirmed\" from \"user\" where \"email\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"organization_id\", \"email\", \"first_name\"," +
        " \"second_name\", \"third_name\", \"phone_number\", \"password\", \"salt\", \"roles\", \"groups\"," +
        " \"version\", \"email_confirmed\" from \"user\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_PHONE_NUMBER = "select \"id\", \"organization_id\", \"email\", \"first_name\"," +
        " \"second_name\", \"third_name\", \"phone_number\", \"password\", \"salt\", \"roles\", \"groups\"," +
        " \"version\", \"email_confirmed\" from \"user\" where \"phone_number\" = ?";


    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"user\" (\"email\", \"password\", \"salt\", " +
        "\"organization_id\", \"roles\", \"first_name\", \"second_name\", \"third_name\", \"phone_number\")" +
        " values (?,?,?,?,?,?,?,?,?)";

    @Language("PostgreSQL")
    private static final String Q_UPDATE = "update \"user\" set \"first_name\" = ?, \"second_name\" = ?," +
        " \"third_name\" = ?, \"phone_number\" = ?,  \"roles\" = ?, \"groups\" = ?, \"version\" = ?," +
        " \"email_confirmed\" = ? where \"id\" = ? and \"version\" = ?";

    @Language("PostgreSQL")
    private static final String Q_EXIST_BY_EMAIL = "select \"id\" from \"user\" where \"email\" = ?";

    private final OrganizationDao organizationDao;
    private final UserGroupDao userGroupDao;

    public JdbcUserDao(
        @NotNull DataSource dataSource,
        @NotNull Executor fastDbTaskExecutor,
        @NotNull Executor slowDbTaskExecutor,
        @NotNull OrganizationDao organizationDao,
        @NotNull UserGroupDao userGroupDao
    ) {
        super(dataSource, fastDbTaskExecutor, slowDbTaskExecutor);
        this.organizationDao = organizationDao;
        this.userGroupDao = userGroupDao;
    }

    @Override
    public @NotNull User create(
        @NotNull String email,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Organization organization,
        @NotNull Set<AccessRole> roles,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @Nullable String phoneNumber
    ) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, email);
            statement.setBytes(2, password);
            statement.setBytes(3, salt);
            statement.setLong(4, organization.getId());
            statement.setObject(5, toJsonArray(roles));
            statement.setString(6, firstName);
            statement.setString(7, secondName);
            statement.setString(8, thirdName);
            statement.setString(9, phoneNumber);
            statement.execute();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new JdbcUser(
                        rs.getLong(1),
                        organization, email,
                        password,
                        salt,
                        roles,
                        firstName,
                        secondName,
                        thirdName,
                        phoneNumber,
                        0
                    );
                } else {
                    throw new GenerateIdDaoException("Can't receive generated id for the new user [" + email + "].");
                }
            }

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<@NotNull User> createAsync(
        @NotNull String email,
        @NotNull byte[] password,
        @NotNull byte[] salt,
        @NotNull Organization organization,
        @NotNull Set<AccessRole> roles,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName,
        @Nullable String phoneNumber
    ) {
        return supplyAsync(() -> create(
            email,
            password,
            salt,
            organization,
            roles,
            firstName,
            secondName,
            thirdName,
            phoneNumber
        ), fastDbTaskExecutor);
    }

    @Override
    public @Nullable User findByEmail(@NotNull String email) {
        return findByString(Q_SELECT_BY_EMAIL, email, JdbcUserDao::toUser);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable User> findByEmailAsync(@NotNull String email) {
        return supplyAsync(() -> findByEmail(email), fastDbTaskExecutor);
    }

    @Override
    public @Nullable User findById(long id) {
        return findByLong(Q_SELECT_BY_ID, id, JdbcUserDao::toUser);
    }

    @Override
    public void update(@NotNull User user) {

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(Q_UPDATE)
        ) {

            int version = user.getVersion();

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getSecondName());
            statement.setString(3, user.getThirdName());
            statement.setString(4, user.getPhoneNumber());
            statement.setObject(5, toJsonArray(user.getRoles()));
            statement.setObject(6, toJsonArray(user.getGroups()));
            statement.setInt(7, version + 1);
            statement.setBoolean(8, user.isEmailConfirmed());
            statement.setLong(9, user.getId());
            statement.setInt(10, version);

            if (statement.executeUpdate() != 1) {
                throw new NotActualObjectDaoException("The user's version " + version + " is outdated.");
            }

            user.setVersion(version + 1);

        } catch (SQLException e) {
            throw JdbcUtils.convert(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> updateAsync(@NotNull User user) {
        return runAsync(() -> update(user), fastDbTaskExecutor);
    }

    @Override
    public boolean existByEmail(@NotNull String email) {
        return existByString(Q_EXIST_BY_EMAIL, email);
    }

    @Override
    public CompletableFuture<Boolean> existByEmailAsync(@NotNull String email) {
        return supplyAsync(() -> existByEmail(email), fastDbTaskExecutor);
    }

    @Override
    public @Nullable User findByPhoneNumber(@NotNull String phoneNumber) {
        return findByString(Q_SELECT_BY_PHONE_NUMBER, phoneNumber, JdbcUserDao::toUser);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable User> findByPhoneNumberAsync(@NotNull String phoneNumber) {
        return supplyAsync(() -> findByPhoneNumber(phoneNumber), fastDbTaskExecutor);
    }

    private @NotNull User toUser(@NotNull ResultSet rs) throws SQLException {

        var org = organizationDao.requireById(rs.getLong(2));
        var roles = JdbcUtils.fromJsonArray(
            rs.getString(10),
            AccessRole::require
        );
        var groups = JdbcUtils.fromJsonArray(
            rs.getString(11),
            userGroupDao,
            Dao::requireById
        );

        return new JdbcUser(
            rs.getLong(1),
            org,
            rs.getString(3),  // email
            rs.getString(4),  // first name
            rs.getString(5),  // second name
            rs.getString(6),  // third name
            rs.getString(7),  // phone number
            rs.getBytes(8),   // password
            rs.getBytes(9),   // salt
            roles,
            groups,
            rs.getInt(12),    // version
            rs.getBoolean(13) // email confirmed
        );
    }

}
