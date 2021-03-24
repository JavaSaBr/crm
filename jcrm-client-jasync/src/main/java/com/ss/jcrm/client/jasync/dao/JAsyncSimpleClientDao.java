package com.ss.jcrm.client.jasync.dao;

import static com.ss.jcrm.jasync.util.JAsyncUtils.*;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.client.api.*;
import com.ss.jcrm.client.api.dao.SimpleClientDao;
import com.ss.jcrm.client.api.impl.*;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncConverter;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.rlib.common.util.array.Array;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncSimpleClientDao extends AbstractJAsyncDao<SimpleClient> implements SimpleClientDao {

    private static final String FIELD_LIST = """
        "id", "organization_id", "assigner", "curators", "first_name", "second_name", "third_name", "birthday", 
        "phone_numbers", "emails", "sites", "messengers", "company", "version", "created", "modified"
        """;

    private static final String Q_SELECT_BY_ID = """
        select ${field-list} from "${schema}"."client" where "id" = ?
        """;

    private static final String Q_SELECT_BY_ORG_ID = """
        select ${field-list} from "${schema}"."client" where "organization_id" = ?
        """;

    private static final String Q_SELECT_BY_ID_AND_ORG_ID = """
        select ${field-list} from "${schema}"."client" where "id" = ? and "organization_id" = ?
        """;

    private static final String Q_SELECT_PAGE_BY_ORG_ID = """
        select ${field-list} from "${schema}"."client" where "organization_id" = ? order by "id" limit ? offset ?
        """;

    private static final String Q_INSERT = """
        insert into "${schema}"."client" 
        ("organization_id", "assigner", "curators", "first_name", "second_name", "third_name", "birthday", 
        "phone_numbers", "emails", "sites", "messengers", "company", "created", "modified")
        values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning id
        """;

    private static final String Q_UPDATE = """
        update "${schema}"."client" set "curators" = ?, "first_name" = ?, "second_name" = ?, "third_name" = ?, 
        "birthday" = ?, "phone_numbers" = ?, "emails" = ?, "sites" = ?, "messengers" = ?, "company" = ?, 
        "modified" = ?, "version" = ? where "id" = ? and "version" = ?
        """;

    private static final String Q_COUNT_BY_ORG_ID = """
        select count("id") from "${schema}"."client" where "organization_id" = ?
        """;

    @NotNull String querySelectById;
    @NotNull String querySelectByIdAndOrgId;
    @NotNull String querySelectByOrgId;
    @NotNull String queryPageByOrgId;
    @NotNull String queryInsert;
    @NotNull String queryUpdate;
    @NotNull String queryCountByOrgId;

    public JAsyncSimpleClientDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool, schema, FIELD_LIST);
        this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
        this.querySelectByIdAndOrgId = prepareQuery(Q_SELECT_BY_ID_AND_ORG_ID);
        this.querySelectByOrgId = prepareQuery(Q_SELECT_BY_ORG_ID);
        this.queryPageByOrgId = prepareQuery(Q_SELECT_PAGE_BY_ORG_ID);
        this.queryInsert = prepareQuery(Q_INSERT);
        this.queryUpdate = prepareQuery(Q_UPDATE);
        this.queryCountByOrgId = prepareQuery(Q_COUNT_BY_ORG_ID);
    }

    @Override
    protected @NotNull Class<SimpleClient> getEntityType() {
        return SimpleClient.class;
    }

    @Override
    public @NotNull Mono<@NotNull SimpleClient> findById(long id) {
        return select(querySelectById, List.of(id), JAsyncSimpleClientDao::toClient);
    }

    @Override
    public @NotNull Mono<@NotNull SimpleClient> create(
        @NotNull User assigner,
        @Nullable Array<User> curators,
        @NotNull Organization organization,
        @NotNull String firstName,
        @NotNull String secondName,
        @Nullable String thirdName,
        @Nullable LocalDate birthday,
        @Nullable ClientPhoneNumber[] phoneNumbers,
        @Nullable ClientEmail[] emails,
        @Nullable ClientSite[] sites,
        @Nullable ClientMessenger[] messengers,
        @Nullable String company
    ) {
        var currentTime = Instant.now();
        return insert(
            queryInsert,
            Arrays.asList(
                organization.getId(),
                assigner.getId(),
                idsToJson(curators),
                firstName,
                secondName,
                thirdName,
                toDate(birthday),
                toJson(phoneNumbers),
                toJson(emails),
                toJson(sites),
                toJson(messengers),
                company,
                toDateTime(currentTime),
                toDateTime(currentTime)
            ),
            id -> new DefaultSimpleClient(
                id,
                assigner.getId(),
                toIds(curators),
                organization.getId(),
                firstName,
                secondName,
                thirdName,
                company,
                birthday,
                currentTime,
                currentTime,
                phoneNumbers,
                emails,
                sites,
                messengers,
                0
            )
        );
    }

    @Override
    public @NotNull Mono<SimpleClient> update(@NotNull SimpleClient contact) {
        contact.setModified(Instant.now());
        return update(
            queryUpdate,
            Arrays.asList(
                toJson(contact.getCuratorIds()),
                contact.getFirstName(),
                contact.getSecondName(),
                contact.getThirdName(),
                toDate(contact.getBirthday()),
                toJson(contact.getPhoneNumbers()),
                toJson(contact.getEmails()),
                toJson(contact.getSites()),
                toJson(contact.getMessengers()),
                contact.getCompany(),
                toDateTime(contact.getModified()),
                contact.getVersion() + 1,
                contact.getId(),
                contact.getVersion()
            ),
            contact
        );
    }

    @Override
    public @NotNull Mono<@NotNull Array<SimpleClient>> findByOrg(long orgId) {
        return selectAll(querySelectByOrgId, List.of(orgId), converter());
    }

    @Override
    public @NotNull Mono<@NotNull EntityPage<SimpleClient>> findPageByOrg(long offset, long size, long orgId) {
        return selectAll(queryPageByOrgId, List.of(orgId, size, offset), converter())
            .zipWith(count(queryCountByOrgId, orgId), EntityPage::new);
    }

    @Override
    public @NotNull Mono<@NotNull SimpleClient> findByIdAndOrg(long id, long orgId) {
        return select(querySelectByIdAndOrgId, List.of(id, orgId), converter());
    }

    private @NotNull JAsyncConverter<JAsyncSimpleClientDao, SimpleClient> converter() {
        return JAsyncSimpleClientDao::toClient;
    }

    private @NotNull SimpleClient toClient(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var organizationId = notNull(data.getLong(1));
        var assignerId = notNull(data.getLong(2));
        var curatorIds = jsonToIds(data.getString(3));

        var firstName = data.getString(4);
        var secondName = data.getString(5);
        var thirdName = data.getString(6);
        var birthday = toJavaDate(data.getAs(7));

        var phoneNumbers = arrayFromJson(
            data.getString(8),
            DefaultClientPhoneNumber[].class,
            SimpleClient.EMPTY_PHONE_NUMBERS
        );

        var emails = arrayFromJson(data.getString(9), DefaultClientEmail[].class, SimpleClient.EMPTY_EMAILS);
        var sites = arrayFromJson(data.getString(10), DefaultContactSite[].class, SimpleClient.EMPTY_SITES);
        var messengers = arrayFromJson(
            data.getString(11),
            DefaultClientMessenger[].class,
            SimpleClient.EMPTY_MESSENGERS
        );

        var company = data.getString(12);
        var version = notNull(data.getInt(13));
        var created = toJavaInstant(data.getAs(14));
        var modified = toJavaInstant(data.getAs(15));

        return new DefaultSimpleClient(
            id,
            assignerId,
            curatorIds,
            organizationId,
            firstName,
            secondName,
            thirdName,
            company,
            birthday,
            created,
            modified,
            phoneNumbers,
            emails,
            sites,
            messengers,
            version
        );
    }
}
