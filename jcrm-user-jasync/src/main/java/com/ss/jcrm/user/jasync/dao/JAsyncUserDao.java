package com.ss.jcrm.user.jasync.dao;

import static com.ss.jcrm.jasync.util.JAsyncUtils.*;
import static com.ss.rlib.common.util.ObjectUtils.ifNull;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncLazyConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.jcrm.user.api.dao.OrganizationDao;
import com.ss.jcrm.user.api.dao.UserDao;
import com.ss.jcrm.user.api.dao.UserGroupDao;
import com.ss.jcrm.user.api.impl.DefaultUser;
import com.ss.jcrm.user.contact.api.Messenger;
import com.ss.jcrm.user.contact.api.PhoneNumber;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.Array;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncUserDao extends AbstractJAsyncDao<User> implements UserDao {

    private static final String FIELD_LIST = """
        "id", "organization_id", "email", "first_name", "second_name", "third_name", "birthday", "phone_numbers", 
        "messengers", "password", "salt", "roles", "groups", "version", "email_confirmed", "password_version", 
        "created", "modified"
        """;

    private static final String Q_SELECT_BY_EMAIL = """
        select ${field-list} from "${schema}"."user" where "email" = ?
        """;

    private static final String Q_SELECT_BY_ID = """
        select ${field-list} from "${schema}"."user" where "id" = ?
        """;

    private static final String Q_SELECT_BY_PHONE_NUMBER = """
        select ${field-list} from "${schema}"."user" where "phone_numbers" like(?)
        """;

    private static final String Q_INSERT = """
        insert into "${schema}"."user" 
          ("email", "password", "salt", "organization_id", "roles", "first_name", "second_name", "third_name", 
          "birthday", "phone_numbers", "messengers", "created", "modified") 
        values (?,?,?,?,?,?,?,?,?,?,?,?,?) returning id
        """;

    private static final String Q_UPDATE = """
        update "${schema}"."user" set 
          "first_name" = ?, "second_name" = ?, "third_name" = ?, "phone_numbers" = ?,  "messengers" = ?, "roles" = ?, 
          "groups" = ?, "version" = ?, "email_confirmed" = ?, "password_version" = ?, "birthday" = ?, "modified" = ? 
        where "id" = ? and "version" = ?
        """;

    private static final String Q_EXIST_BY_EMAIL = """
        select "id" from "${schema}"."user" where "email" = ?
        """;

    private static final String Q_SEARCH_BY_NAME = """
        select ${field-list} from "${schema}"."user" 
        where (("first_name" || ' ' || "second_name" || ' ' || "third_name" ilike (?)) OR "email" ilike (?)) 
          AND "organization_id" = ?
        """;

    private static final String Q_SELECT_BY_ID_AND_ORG_ID = """
        select ${field-list} from "${schema}"."user" where "id" = ? AND "organization_id" = ?
        """;

    private static final String Q_SELECT_BY_IDS_AND_ORG_ID = """
        select ${field-list} from "${schema}"."user" where "id" in (${id-list}) AND "organization_id" = ?
        """;

    private static final String Q_SELECT_PAGE_BY_ORG_ID = """
        select ${field-list} from "${schema}"."user" where "organization_id" = ? order by "id" limit ? offset ?
        """;

    private static final String Q_COUNT_BY_ORG_ID = """
        select count("id") from "${schema}"."user" where "organization_id" = ?
        """;

    private static final String Q_COUNT_BY_USERS_WHICH_NOT_IN_ORG = """
        select count("id") from "${schema}"."user" where "organization_id" != ? and "id" in (${id-list})
        """;

    @NotNull String querySelectById;
    @NotNull String querySelectByEmail;
    @NotNull String querySelectByPhoneNumber;
    @NotNull String querySelectByIdAndOrgId;
    @NotNull String querySelectByIdsAndOrgId;
    @NotNull String queryExistByEmail;
    @NotNull String querySearchByName;
    @NotNull String queryInsert;
    @NotNull String queryUpdate;
    @NotNull String queryPageByOrgId;
    @NotNull String queryCountByOrgId;
    @NotNull String queryCountByUsersWhichNotInOrg;

    @NotNull OrganizationDao organizationDao;
    @NotNull UserGroupDao userGroupDao;

    public JAsyncUserDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema,
        @NotNull OrganizationDao organizationDao,
        @NotNull UserGroupDao userGroupDao
    ) {
        super(connectionPool, schema, FIELD_LIST);
        this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
        this.querySelectByEmail = prepareQuery(Q_SELECT_BY_EMAIL);
        this.querySelectByPhoneNumber = prepareQuery(Q_SELECT_BY_PHONE_NUMBER);
        this.querySelectByIdAndOrgId = prepareQuery(Q_SELECT_BY_ID_AND_ORG_ID);
        this.querySelectByIdsAndOrgId = prepareQuery(Q_SELECT_BY_IDS_AND_ORG_ID);
        this.querySearchByName = prepareQuery(Q_SEARCH_BY_NAME);
        this.queryExistByEmail = prepareQuery(Q_EXIST_BY_EMAIL);
        this.queryPageByOrgId = prepareQuery(Q_SELECT_PAGE_BY_ORG_ID);
        this.queryInsert = prepareQuery(Q_INSERT);
        this.queryUpdate = prepareQuery(Q_UPDATE);
        this.queryCountByOrgId = prepareQuery(Q_COUNT_BY_ORG_ID);
        this.queryCountByUsersWhichNotInOrg = prepareQuery(Q_COUNT_BY_USERS_WHICH_NOT_IN_ORG);
        this.organizationDao = organizationDao;
        this.userGroupDao = userGroupDao;
    }

    @Override
    protected @NotNull Class<User> getEntityType() {
        return User.class;
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
        @Nullable LocalDate birthday,
        @NotNull Set<PhoneNumber> phoneNumbers,
        @NotNull Set<Messenger> messengers
    ) {

        var currentTime = Instant.now();
        return insert(
            queryInsert,
            Arrays.asList(
                email,
                password,
                salt,
                organization.getId(),
                JAsyncUtils.idsToJson(roles),
                StringUtils.emptyIfNull(firstName),
                StringUtils.emptyIfNull(secondName),
                StringUtils.emptyIfNull(thirdName),
                toDate(birthday),
                JAsyncUtils.toJson(phoneNumbers),
                JAsyncUtils.toJson(messengers),
                toDateTime(currentTime),
                toDateTime(currentTime)
            ),
            id -> new DefaultUser(
                id,
                organization,
                email,
                password,
                salt,
                roles,
                firstName,
                secondName,
                thirdName,
                birthday,
                phoneNumbers,
                messengers,
                currentTime,
                currentTime,
                0,
                0
            )
        );
    }

    @Override
    public @NotNull Mono<@NotNull User> findByEmail(@NotNull String email) {
        return selectAsync(querySelectByEmail, email, converter());
    }

    @Override
    public @NotNull Mono<@NotNull User> findById(long id) {
        return selectAsync(querySelectById, id, converter());
    }

    @Override
    public @NotNull Mono<@NotNull User> findByIdAndOrgId(long id, long orgId) {
        return selectAsync(querySelectByIdAndOrgId, List.of(id, orgId), converter());
    }

    @Override
    public @NotNull Mono<@NotNull Array<User>> findByIdsAndOrgId(@Nullable long[] ids, long orgId) {

        if (ids == null || ids.length == 0) {
            return Mono.just(Array.empty());
        } else if (ids.length == 1) {
            return selectAllAsync(User.class, querySelectByIdAndOrgId, List.of(ids[0], orgId), converter());
        }

        var args = new ArrayList<>(ids.length + 1);

        for (var id : ids) {
            args.add(id);
        }

        args.add(orgId);

        var query = injectIdList(querySelectByIdsAndOrgId, ids);

        return selectAllAsync(User.class, query, args, converter());
    }

    @Override
    public @NotNull Mono<User> update(@NotNull User user) {

        user.setModified(Instant.now());

        return update(
            queryUpdate,
            Arrays.asList(
                StringUtils.emptyIfNull(user.getFirstName()),
                StringUtils.emptyIfNull(user.getSecondName()),
                StringUtils.emptyIfNull(user.getThirdName()),
                JAsyncUtils.toJson(user.getPhoneNumbers()),
                JAsyncUtils.toJson(user.getMessengers()),
                JAsyncUtils.idsToJson(user.getRoles()),
                JAsyncUtils.idsToJson(user.getGroups()),
                user.getVersion() + 1,
                user.isEmailConfirmed(),
                user.getPasswordVersion(),
                toDate(user.getBirthday()),
                toDateTime(user.getModified()),
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
        return selectAsync(querySelectByPhoneNumber, List.of("%\"fullPhoneNumber\":\"" + phoneNumber + "\"%"), converter());
    }

    @Override
    public @NotNull Mono<@NotNull Array<User>> searchByName(@NotNull String name, long orgId) {
        var pattern = "%" + name + "%";
        return selectAllAsync(
            User.class,
            querySearchByName,
            List.of(pattern, pattern, orgId), converter()
        );
    }

    private @NotNull JAsyncLazyConverter<@NotNull JAsyncUserDao, @NotNull User> converter() {
        return JAsyncUserDao::toUser;
    }

    @Override
    public @NotNull Mono<@NotNull EntityPage<User>> findPageByOrg(long offset, long size, long orgId) {
        return selectAllAsync(queryPageByOrgId, List.of(orgId, size, offset), converter())
            .zipWith(count(queryCountByOrgId, orgId), EntityPage::new);
    }

    @Override
    public @NotNull Mono<Boolean> containsAll(@NotNull long[] ids, long orgId) {

        if (ids == null || ids.length == 0) {
            return Mono.just(true);
        }

        var args = new ArrayList<>(ids.length + 1);
        args.add(orgId);

        for (var id : ids) {
            args.add(id);
        }

        var query = injectIdList(queryCountByUsersWhichNotInOrg, ids);

        return count(query, args).map(count -> count < 1);
    }

    private @NotNull Mono<@NotNull User> toUser(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var version = notNull(data.getInt(13));

        var orgId = ifNull(data.getLong(1), 0L);
        var email = notNull(data.getString(2));
        var firstName = data.getString(3);
        var secondName = data.getString(4);
        var thirdName = data.getString(5);
        var birthday = toJavaDate(data.getAs(6));
        var phoneNumbers = JAsyncUtils.fromJsonArrayToSet(data.getString(7), PhoneNumber[].class);
        var messengers = JAsyncUtils.fromJsonArrayToSet(data.getString(8), Messenger[].class);
        var passwordVersion = ifNull(data.getInt(15), 0);
        var emailConfirmed = ifNull(data.getBoolean(14), Boolean.FALSE);

        byte[] password = data.getAs(9);
        byte[] salt = data.getAs(10);

        var roles = JAsyncUtils.fromJsonIds(
            data.getString(11),
            AccessRole::require
        );

        var orgAsync = organizationDao.requireById(orgId);
        var groupsAsync = JAsyncUtils.fromJsonIdsAsync(
            data.getString(12),
            userGroupDao,
            Dao::requireById
        );

        var created = toJavaInstant(data.getAs(16));
        var modified = toJavaInstant(data.getAs(17));

        return Mono.zip(groupsAsync, orgAsync)
            .map(objects -> new DefaultUser(
                id,
                objects.getT2(),
                created,
                email,
                firstName,
                secondName,
                thirdName,
                birthday,
                modified,
                password,
                salt,
                roles,
                objects.getT1(),
                phoneNumbers,
                messengers,
                version,
                passwordVersion,
                emailConfirmed
            ));
    }
}
