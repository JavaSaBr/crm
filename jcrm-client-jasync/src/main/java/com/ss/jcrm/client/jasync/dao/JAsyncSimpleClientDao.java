package com.ss.jcrm.client.jasync.dao;

import static crm.base.util.DateUtils.toUtcInstant;
import static jasync.util.JAsyncUtils.*;
import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.base.util.DateUtils;
import com.ss.jcrm.client.api.*;
import com.ss.jcrm.client.api.dao.SimpleClientDao;
import com.ss.jcrm.client.api.impl.*;
import com.ss.jcrm.dao.EntityPage;
import jasync.dao.AbstractJAsyncDao;
import jasync.function.JAsyncConverter;
import jasync.util.JAsyncUtils;
import crm.user.api.Organization;
import crm.user.api.User;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncSimpleClientDao extends AbstractJAsyncDao<SimpleClient> implements SimpleClientDao {

  private static final CollectionType CLIENT_EMAILS = JAsyncUtils.collectionType(ArrayList.class, DefaultClientEmail.class);
  private static final CollectionType CONTACT_SITES = JAsyncUtils.collectionType(ArrayList.class, DefaultContactSite.class);
  private static final CollectionType CLIENT_MESSENGERS = JAsyncUtils.collectionType(ArrayList.class, DefaultClientMessenger.class);
  private static final CollectionType PHONE_NUMBERS = JAsyncUtils.collectionType(ArrayList.class, DefaultClientPhoneNumber.class);

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
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool, @NotNull String schema) {
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
      @Nullable List<User> curators,
      @NotNull Organization organization,
      @NotNull String firstName,
      @NotNull String secondName,
      @Nullable String thirdName,
      @Nullable LocalDate birthday,
      @NotNull List<ClientPhoneNumber> phoneNumbers,
      @NotNull List<ClientEmail> emails,
      @NotNull List<ClientSite> sites,
      @NotNull List<ClientMessenger> messengers,
      @Nullable String company) {

    var currentTime = LocalDateTime.now();
    var utcTime = DateUtils.toUtcInstant(currentTime);
    var args = Arrays.asList(
        organization.id(),
        assigner.id(),
        idsToJson(curators),
        firstName,
        secondName,
        thirdName,
        birthday,
        toJson(phoneNumbers),
        toJson(emails),
        toJson(sites),
        toJson(messengers),
        company,
        currentTime,
        currentTime);

    return insert(queryInsert, args)
        .map(id -> new DefaultSimpleClient(
            id,
            assigner.id(),
            toIds(curators),
            organization.id(),
            firstName,
            secondName,
            thirdName,
            company,
            birthday,
            utcTime,
            utcTime,
            phoneNumbers,
            emails,
            sites,
            messengers,
            0));
  }

  @Override
  public @NotNull Mono<SimpleClient> update(@NotNull SimpleClient contact) {
    contact.modified(Instant.now());
    return update(queryUpdate, Arrays.asList(toJson(contact.curatorIds()),
        contact.firstName(),
        contact.secondName(),
        contact.thirdName(),
        contact.birthday(),
        toJson(contact.phoneNumbers()),
        toJson(contact.emails()),
        toJson(contact.sites()),
        toJson(contact.messengers()),
        contact.company(),
        DateUtils.toLocalDateTime(contact.modified()),
        contact.version() + 1,
        contact.id(),
        contact.version()), contact);
  }

  @Override
  public @NotNull Flux<SimpleClient> findByOrg(long orgId) {
    return selectAll(querySelectByOrgId, List.of(orgId), converter());
  }

  @Override
  public @NotNull Mono<EntityPage<SimpleClient>> findPageByOrg(long offset, long size, long orgId) {
    return selectAll(queryPageByOrgId, List.of(orgId, size, offset), converter())
        .collectList()
        .zipWith(count(queryCountByOrgId, orgId), EntityPage::new);
  }

  @Override
  public @NotNull Mono<SimpleClient> findByIdAndOrg(long id, long orgId) {
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
    var birthday = (LocalDate) data.getAs(7);

    List<ClientPhoneNumber> phoneNumbers = listFromJson(data.getString(8), PHONE_NUMBERS);
    List<ClientEmail> emails = listFromJson(data.getString(9), CLIENT_EMAILS);
    List<ClientSite> sites = listFromJson(data.getString(10), CONTACT_SITES);
    List<ClientMessenger> messengers = listFromJson(data.getString(11), CLIENT_MESSENGERS);

    var company = data.getString(12);
    var version = notNull(data.getInt(13));
    var created = toUtcInstant(data.getAs(14));
    var modified = toUtcInstant(data.getAs(15));

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
        version);
  }
}
