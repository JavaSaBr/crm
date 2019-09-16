package com.ss.jcrm.client.jasync.dao;

import static com.ss.jcrm.jasync.util.JAsyncUtils.*;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.client.api.*;
import com.ss.jcrm.client.api.impl.*;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.user.api.Organization;
import com.ss.jcrm.user.api.User;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class JAsyncSimpleContactDao extends AbstractJAsyncDao<SimpleContact> implements SimpleContactDao {

    private static final String CONTACT_FIELDS = "\"id\", \"org_id\", \"assigner\", \"curators\", \"first_name\"," +
        " \"second_name\", \"third_name\", \"birthday\", \"phone_numbers\", \"emails\", \"sites\", \"messengers\"," +
        " \"company\", \"version\"";

    private static final String Q_SELECT_BY_ID = "select " + CONTACT_FIELDS + " from \"${schema}\".\"contact\"" +
        " where \"id\" = ?";

    private static final String Q_SELECT_BY_ORG_ID = "select " + CONTACT_FIELDS + " from \"${schema}\".\"contact\"" +
        " where \"org_id\" = ?";

    private static final String Q_SELECT_BY_ID_AND_ORG_ID = "select " + CONTACT_FIELDS + " from" +
        " \"${schema}\".\"contact\" where \"id\" = ? AND \"org_id\" = ?";

    private static final String Q_INSERT = "insert into \"${schema}\".\"contact\" (\"org_id\", \"assigner\", \"curators\", " +
        "\"first_name\", \"second_name\", \"third_name\", \"birthday\", \"phone_numbers\", \"emails\", \"sites\", " +
        "\"messengers\", \"company\") values (?,?,?,?,?,?,?,?,?,?,?,?) returning id";

    private static final String Q_UPDATE = "update \"${schema}\".\"contact\" set \"curators\" = ?, \"first_name\" = ?," +
        " \"second_name\" = ?, \"third_name\" = ?, \"birthday\" = ?, \"phone_numbers\" = ?, \"emails\" = ?," +
        " \"sites\" = ?, \"messengers\" = ?, \"company\" = ? where \"id\" = ? and \"version\" = ?";

    private final String querySelectById;
    private final String querySelectByIdAndOrgId;
    private final String querySelectByOrgId;
    private final String queryInsert;
    private final String queryUpdate;

    public JAsyncSimpleContactDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
        @NotNull String schema
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectByIdAndOrgId = Q_SELECT_BY_ID_AND_ORG_ID.replace("${schema}", schema);
        this.querySelectByOrgId = Q_SELECT_BY_ORG_ID.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.queryUpdate = Q_UPDATE.replace("${schema}", schema);
    }

    @Override
    public @NotNull Mono<@Nullable SimpleContact> findById(long id) {
        return select(querySelectById, List.of(id), JAsyncSimpleContactDao::toContact);
    }

    @Override
    public @NotNull Mono<@NotNull SimpleContact> create(
        @NotNull User assigner,
        @Nullable User[] curators,
        @NotNull Organization organization,
        @NotNull String firstName,
        @NotNull String secondName,
        @Nullable String thirdName,
        @Nullable LocalDate birthday,
        @Nullable ContactPhoneNumber[] phoneNumbers,
        @Nullable ContactEmail[] emails,
        @Nullable ContactSite[] sites,
        @Nullable ContactMessenger[] messengers,
        @Nullable String company
    ) {
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
                company
            ),
            id -> new DefaultSimpleContact(
                id,
                assigner.getId(),
                toIds(curators),
                organization.getId(),
                firstName,
                secondName,
                thirdName,
                company,
                birthday,
                phoneNumbers,
                emails,
                sites,
                messengers,
                0
            )
        );
    }

    @Override
    public @NotNull Mono<Void> update(@NotNull SimpleContact contact) {
        return update(
            queryUpdate,
            Arrays.asList(
                contact.getCuratorIds(),
                contact.getFirstName(),
                contact.getSecondName(),
                contact.getThirdName(),
                toDate(contact.getBirthday()),
                toJson(contact.getPhoneNumbers()),
                toJson(contact.getEmails()),
                toJson(contact.getSites()),
                toJson(contact.getMessengers()),
                contact.getCompany(),
                contact.getId(),
                contact.getVersion()
            ),
            contact
        );
    }

    @Override
    public @NotNull Mono<@NotNull Array<SimpleContact>> findByOrg(long orgId) {
        return selectAll(
            SimpleContact.class,
            querySelectByOrgId,
            List.of(orgId),
            JAsyncSimpleContactDao::toContact
        );
    }

    @Override
    public @NotNull Mono<@Nullable SimpleContact> findByIdAndOrg(long id, long orgId) {
        return select(
            querySelectByIdAndOrgId,
            List.of(id, orgId),
            JAsyncSimpleContactDao::toContact
        );
    }

    private @NotNull SimpleContact toContact(@NotNull RowData data) {

        var id = notNull(data.getLong(0));
        var organizationId = notNull(data.getLong(1));
        var assignerId = notNull(data.getLong(2));
        var curatorIds = jsonToIds(data.getString(3));

        var firstName = data.getString(4);
        var secondName = data.getString(5);
        var thirdName = data.getString(6);
        var birthday = toJavaDate(data.getAs(7));

        var phoneNumbers = arrayFromJson(data.getString(8), DefaultContactPhoneNumber[].class);
        var emails = arrayFromJson(data.getString(9), DefaultContactEmail[].class);
        var sites = arrayFromJson(data.getString(10), DefaultContactSite[].class);
        var messengers = arrayFromJson(data.getString(11), DefaultContactMessenger[].class);

        var company = data.getString(12);
        var version = notNull(data.getInt(13));

        return new DefaultSimpleContact(
            id,
            assignerId,
            curatorIds,
            organizationId,
            firstName,
            secondName,
            thirdName,
            company,
            birthday,
            phoneNumbers,
            emails,
            sites,
            messengers,
            version
        );
    }
}
