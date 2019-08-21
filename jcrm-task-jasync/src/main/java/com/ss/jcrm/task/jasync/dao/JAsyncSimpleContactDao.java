package com.ss.jcrm.task.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.task.api.SimpleContact;
import com.ss.jcrm.task.api.dao.SimpleContactDao;
import com.ss.jcrm.task.api.impl.DefaultSimpleContact;
import com.ss.jcrm.user.api.Organization;
import com.ss.rlib.common.util.array.Array;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JAsyncSimpleContactDao extends AbstractJAsyncDao<SimpleContact> implements SimpleContactDao {

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ID = "select \"id\", \"org_id\", \"first_name\", \"second_name\", " +
        " \"third_name\", \"version\" from \"${schema}\".\"contact\" where \"id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_SELECT_BY_ORG_ID = "select \"id\", \"org_id\", \"first_name\", \"second_name\", " +
        " \"third_name\", \"version\" from \"${schema}\".\"contact\" where \"org_id\" = ?";

    @Language("PostgreSQL")
    private static final String Q_INSERT = "insert into \"${schema}\".\"contact\" (\"org_id\", \"first_name\", \"second_name\", " +
        "\"third_name\")" +
        " values (?,?,?,?)";

    @Language("PostgreSQL")
    private static final String Q_UPDATE = "update \"${schema}\".\"contact\" set \"first_name\" = ?, \"second_name\" = ?," +
        " \"third_name\" = ? where \"id\" = ? and \"version\" = ?";

    private final String querySelectById;
    private final String querySelectByOrgId;
    private final String queryInsert;
    private final String queryUpdate;

    public JAsyncSimpleContactDao(
        @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool, @NotNull String schema
    ) {
        super(connectionPool);
        this.querySelectById = Q_SELECT_BY_ID.replace("${schema}", schema);
        this.querySelectByOrgId = Q_SELECT_BY_ORG_ID.replace("${schema}", schema);
        this.queryInsert = Q_INSERT.replace("${schema}", schema);
        this.queryUpdate = Q_UPDATE.replace("${schema}", schema);
    }

    @Override
    public @NotNull Mono<@NotNull SimpleContact> create(
        @NotNull Organization organization,
        @Nullable String firstName,
        @Nullable String secondName,
        @Nullable String thirdName
    ) {
        return Mono.fromFuture(insert(
            queryInsert,
            Arrays.asList(organization.getId(), firstName, secondName, thirdName),
            (dao, id) -> new DefaultSimpleContact(id, organization.getId(), firstName, secondName, thirdName, 0)
        ));
    }

    @Override
    public @NotNull Mono<Void> update(@NotNull SimpleContact contact) {
        return Mono.fromFuture(update(
            queryInsert,
            Arrays.asList(
                contact.getFirstName(),
                contact.getSecondName(),
                contact.getThirdName(),
                contact.getId(),
                contact.getVersion()
            ),
            contact
        ));
    }

    @Override
    public @NotNull Mono<Array<SimpleContact>> findByOrg(@NotNull Organization organization) {
        return Mono.fromFuture(findAll(
            SimpleContact.class,
            querySelectByOrgId,
            List.of(organization.getId()),
            JAsyncSimpleContactDao::toContact
        ));
    }

    @Override
    public @NotNull CompletableFuture<SimpleContact> findByIdAsync(long id) {
        return findBy(querySelectById, id, JAsyncSimpleContactDao::toContact);
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
