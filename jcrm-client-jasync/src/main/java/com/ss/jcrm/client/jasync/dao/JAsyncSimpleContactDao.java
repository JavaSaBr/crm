package com.ss.jcrm.client.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.client.api.SimpleContact;
import com.ss.jcrm.client.api.dao.SimpleContactDao;
import com.ss.jcrm.client.api.impl.DefaultSimpleContact;
import com.ss.jcrm.user.api.Organization;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class JAsyncSimpleContactDao extends AbstractJAsyncDao<SimpleContact> implements SimpleContactDao {

    private static final String Q_SELECT_BY_ID = "select \"id\", \"org_id\", \"first_name\", \"second_name\", " +
        " \"third_name\", \"version\" from \"${schema}\".\"contact\" where \"id\" = ?";

    private static final String Q_SELECT_BY_ORG_ID = "select \"id\", \"org_id\", \"first_name\", \"second_name\", " +
        " \"third_name\", \"version\" from \"${schema}\".\"contact\" where \"org_id\" = ?";

    private static final String Q_SELECT_BY_ID_AND_ORG_ID = "select \"id\", \"org_id\", \"first_name\", \"second_name\", " +
        " \"third_name\", \"version\" from \"${schema}\".\"contact\" where \"id\" = ? AND \"org_id\" = ?";

    private static final String Q_INSERT = "insert into \"${schema}\".\"contact\" (\"org_id\", \"first_name\", \"second_name\", " +
        "\"third_name\") values (?,?,?,?) returning id";

    private static final String Q_UPDATE = "update \"${schema}\".\"contact\" set \"first_name\" = ?, \"second_name\" = ?," +
        " \"third_name\" = ? where \"id\" = ? and \"version\" = ?";

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
        @NotNull Organization organization,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName
    ) {
        return insert(
            queryInsert,
            Arrays.asList(organization.getId(), firstName, secondName, thirdName),
            id -> new DefaultSimpleContact(id, organization.getId(), firstName, secondName, thirdName, 0)
        );
    }

    @Override
    public @NotNull Mono<Void> update(@NotNull SimpleContact contact) {
        return update(
            queryUpdate,
            Arrays.asList(
                contact.getFirstName(),
                contact.getSecondName(),
                contact.getThirdName(),
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
        var firstName = data.getString(2);
        var secondName = data.getString(3);
        var thirdName = data.getString(4);
        var version = notNull(data.getInt(5));

        return new DefaultSimpleContact(id, organizationId, firstName, secondName, thirdName, version);
    }
}
