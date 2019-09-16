package com.ss.jcrm.user.jasync.dao;

import static com.ss.jcrm.jasync.util.JAsyncUtils.fromJsonIdsAsync;
import static com.ss.rlib.common.util.ObjectUtils.ifNull;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.api.impl.DefaultUser;
import com.ss.rlib.common.util.array.Array;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Log4j2
public class JAsyncUserDao extends AbstractJAsyncDao<User> implements UserDao {

    private static final String USER_FIELDS = "\"id\", \"organization_id\", \"email\", \"first_name\"," +
        " \"second_name\", \"third_name\", \"phone_number\", \"password\", \"salt\", \"roles\", \"groups\"," +
        " \"version\", \"email_confirmed\", \"password_version\"";

    private static final String Q_SELECT_BY_EMAIL = "select " + USER_FIELDS +
        " from \"${schema}\".\"user\" where \"email\" = ?";

    private static final String Q_SELECT_BY_ID = "select " + USER_FIELDS +
        " from \"${schema}\".\"user\" where \"id\" = ?";

    private static final String Q_SELECT_BY_PHONE_NUMBER = "select " + USER_FIELDS +
        " from \"${schema}\".\"user\" where \"phone_number\" = ?";

    private static final String Q_INSERT = "insert into \"${schema}\".\"user\" (\"email\", \"password\", \"salt\", " +
        "\"organization_id\", \"roles\", \"first_name\", \"second_name\", \"third_name\", \"phone_number\")" +
        " values (?,?,?,?,?,?,?,?,?) RETURNING id";

    private static final String Q_UPDATE = "update \"${schema}\".\"user\" set \"first_name\" = ?, \"second_name\" = ?," +
        " \"third_name\" = ?, \"phone_number\" = ?,  \"roles\" = ?, \"groups\" = ?, \"version\" = ?," +
        " \"email_confirmed\" = ?, \"password_version\" = ? where \"id\" = ? and \"version\" = ?";

    private static final String Q_EXIST_BY_EMAIL = "select \"id\" from \"${schema}\".\"user\" where \"email\" = ?";

    private static final String Q_SEARCH_BY_NAME = "select " + USER_FIELDS +
        " from \"${schema}\".\"user\" where ((\"first_name\" || ' ' || \"second_name\" || ' ' ||" +
        " \"third_name\" ilike (?)) OR \"email\" ilike (?)) AND \"organization_id\" = ?";

    private static final String Q_SELECT_BY_ID_AND_ORG_ID = "select " + USER_FIELDS +
        " from \"${schema}\".\"user\" where \"id\" = ? AND \"organization_id\" = ?";

    private final String querySelectById;
    private final String querySelectByEmail;
    private final String querySelectByPhoneNumber;
    private final String queryInsert;
    private final String queryUpdate;
    private final String queryExistByEmail;
    private final String querySearchByName;
    private final String querySelectByIdAndOrgId;

    private final OrganizationDao organizationDao;
    private final UserGroupDao userGroupDao;

    public JAsyncUserDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull OrganizationDao organizationDao,
        @NotNull UserGroupDao userGroupDao
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectByEmail = Q_SELECT_BY_EMAIL.replace("${schema}", schema);
        this.querySelectByPhoneNumber = Q_SELECT_BY_PHONE_NUMBER.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.queryUpdate = Q_UPDATE.replace("${schema}", schema);
        this.queryExistByEmail = Q_EXIST_BY_EMAIL.replace("${schema}", schema);
        this.querySearchByName = Q_SEARCH_BY_NAME.replace("${schema}", schema);
        this.querySelectByIdAndOrgId = Q_SELECT_BY_ID_AND_ORG_ID.replace("${schema}", schema);
        this.organizationDao = organizationDao;
        this.userGroupDao = userGroupDao;
    }

    @Override
    public @NotNull Mono<@NotNull User> create(
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

        return insert(
            queryInsert,
            Arrays.asList(
                email,
                password,
                salt,
                organization.getId(),
                JAsyncUtils.idsToJson(roles),
                firstName,
                secondName,
                thirdName,
                phoneNumber
            ),
            id -> new DefaultUser(
                id,
                organization, email,
                password,
                salt,
                roles,
                firstName,
                secondName,
                thirdName,
                phoneNumber,
                0,
                0
            )
        );
    }

    @Override
    public @NotNull Mono<@Nullable User> findByEmail(@NotNull String email) {
        return selectAsync(querySelectByEmail, List.of(email), JAsyncUserDao::toUser);
    }

    @Override
    public @NotNull Mono<@Nullable User> findById(long id) {
        return selectAsync(querySelectById, List.of(id), JAsyncUserDao::toUser);
    }

    @Override
    public @NotNull Mono<@Nullable User> findByIdAndOrgId(long id, long orgId) {
        return selectAsync(querySelectByIdAndOrgId, List.of(id, orgId), JAsyncUserDao::toUser);
    }

    @Override
    public @NotNull Mono<Void> update(@NotNull User user) {
        return update(
            queryUpdate,
            Arrays.asList(
                user.getFirstName(),
                user.getSecondName(),
                user.getThirdName(),
                user.getPhoneNumber(),
                JAsyncUtils.idsToJson(user.getRoles()),
                JAsyncUtils.idsToJson(user.getGroups()),
                user.getVersion() + 1,
                user.isEmailConfirmed(),
                user.getPasswordVersion(),
                user.getId(),
                user.getVersion()
            ),
            user
        );
    }

    @Override
    public @NotNull Mono<Boolean> existByEmail(@NotNull String email) {
        return exist(queryExistByEmail, List.of(email));
    }

    @Override
    public @NotNull Mono<@Nullable User> findByPhoneNumber(@NotNull String phoneNumber) {
        return selectAsync(querySelectByPhoneNumber, List.of(phoneNumber), JAsyncUserDao::toUser);
    }

    @Override
    public @NotNull Mono<@NotNull Array<User>> searchByName(@NotNull String name, long orgId) {
        var pattern = "%" + name + "%";
        return selectAllAsync(
            User.class,
            querySearchByName,
            List.of(pattern, pattern, orgId),
            JAsyncUserDao::toUser
        );
    }

    private @NotNull Mono<@NotNull User> toUser(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var version = notNull(data.getInt(11));

        var orgId = ifNull(data.getLong(1), 0L);
        var email = data.getString(2);
        var firstName = data.getString(3);
        var secondName = data.getString(4);
        var thirdName = data.getString(5);
        var phoneNumber = data.getString(6);
        var passwordVersion = ifNull(data.getInt(13), 0);
        var emailConfirmed = ifNull(data.getBoolean(12), Boolean.FALSE);

        byte[] password = data.getAs(7);
        byte[] salt = data.getAs(8);

        var roles = JAsyncUtils.fromJsonIds(
            data.getString(9),
            AccessRole::require
        );

        var orgAsync = organizationDao.requireById(orgId);
        var groupsAsync = JAsyncUtils.fromJsonIdsAsync(
            data.getString(10),
            userGroupDao,
            Dao::requireById
        );

        return Flux.concat(groupsAsync, orgAsync)
            .last().map(ignore -> new DefaultUser(
                id,
                orgAsync.block(),
                email,
                firstName,
                secondName,
                thirdName,
                phoneNumber,
                password,
                salt,
                roles,
                groupsAsync.block(),
                version,
                passwordVersion,
                emailConfirmed
            ));
    }

}
