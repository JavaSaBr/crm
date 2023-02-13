package crm.client.jasync.dao;

import static crm.base.util.DateUtils.toUtcInstant;
import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.base.util.DateUtils;
import crm.client.api.dao.SimpleClientDao;
import crm.dao.EntityPage;
import crm.client.api.SimpleClient;
import crm.client.api.impl.DefaultSimpleClient;
import crm.contact.api.Email;
import crm.contact.api.Messenger;
import crm.contact.api.PhoneNumber;
import crm.contact.api.Site;
import crm.contact.api.impl.DefaultPhoneNumber;
import jasync.dao.AbstractJAsyncDao;
import jasync.function.JAsyncConverter;
import jasync.util.JAsyncUtils;
import crm.user.api.Organization;
import crm.user.api.User;
import java.util.HashSet;
import java.util.Set;
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

  private static final CollectionType CLIENT_EMAILS = JAsyncUtils.collectionType(HashSet.class, Email.class);
  private static final CollectionType CONTACT_SITES = JAsyncUtils.collectionType(HashSet.class, Site.class);
  private static final CollectionType CLIENT_MESSENGERS = JAsyncUtils.collectionType(HashSet.class, Messenger.class);
  private static final CollectionType PHONE_NUMBERS = JAsyncUtils.collectionType(
      HashSet.class,
      DefaultPhoneNumber.class);

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
  public @NotNull Mono<SimpleClient> findById(long id) {
    return select(querySelectById, List.of(id), JAsyncSimpleClientDao::toClient);
  }

  @Override
  public @NotNull Mono<SimpleClient> create(
      @NotNull User assigner,
      @NotNull Set<User> curators,
      @NotNull Organization organization,
      @Nullable String firstName,
      @Nullable String secondName,
      @Nullable String thirdName,
      @Nullable LocalDate birthday,
      @NotNull Set<PhoneNumber> phoneNumbers,
      @NotNull Set<Email> emails,
      @NotNull Set<Site> sites,
      @NotNull Set<Messenger> messengers,
      @Nullable String company) {

    var currentTime = LocalDateTime.now();
    var utcTime = DateUtils.toUtcInstant(currentTime);
    var args = Arrays.asList(organization.id(),
        assigner.id(),
        JAsyncUtils.idsToJson(curators),
        firstName,
        secondName,
        thirdName,
        birthday,
        JAsyncUtils.toJson(phoneNumbers),
        JAsyncUtils.toJson(emails),
        JAsyncUtils.toJson(sites),
        JAsyncUtils.toJson(messengers),
        company,
        currentTime,
        currentTime);

    return insert(queryInsert, args)
        .map(id -> new DefaultSimpleClient(id,
            assigner.id(),
            JAsyncUtils.toIds(curators),
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

    var args = Arrays.asList(
        JAsyncUtils.toJson(contact.curatorIds()),
        contact.firstName(),
        contact.secondName(),
        contact.thirdName(),
        contact.birthday(),
        JAsyncUtils.toJson(contact.phoneNumbers()),
        JAsyncUtils.toJson(contact.emails()),
        JAsyncUtils.toJson(contact.sites()),
        JAsyncUtils.toJson(contact.messengers()),
        contact.company(),
        DateUtils.toLocalDateTime(contact.modified()),
        contact.version() + 1,
        contact.id(),
        contact.version());

    return update(queryUpdate, args, contact);
  }

  @Override
  public @NotNull Flux<SimpleClient> findByOrganization(long organizationId) {
    return selectAll(querySelectByOrgId, List.of(organizationId), converter());
  }

  @Override
  public @NotNull Mono<EntityPage<SimpleClient>> findPageByOrg(long offset, long size, long orgId) {
    return selectAll(queryPageByOrgId, List.of(orgId, size, offset), converter())
        .collectList()
        .zipWith(count(queryCountByOrgId, orgId), EntityPage::new);
  }

  @Override
  public @NotNull Mono<SimpleClient> findByIdAndOrganization(long id, long organizationId) {
    return select(querySelectByIdAndOrgId, List.of(id, organizationId), converter());
  }

  private @NotNull JAsyncConverter<JAsyncSimpleClientDao, SimpleClient> converter() {
    return JAsyncSimpleClientDao::toClient;
  }

  private @NotNull SimpleClient toClient(@NotNull RowData data) {

    var id = notNull(data.getLong(0));
    var organizationId = notNull(data.getLong(1));
    var assignerId = notNull(data.getLong(2));
    var curatorIds = JAsyncUtils.jsonToIds(data.getString(3));

    var firstName = data.getString(4);
    var secondName = data.getString(5);
    var thirdName = data.getString(6);
    var birthday = (LocalDate) data.getAs(7);

    Set<PhoneNumber> phoneNumbers = JAsyncUtils.setFromJson(data.getString(8), PHONE_NUMBERS);
    Set<Email> emails = JAsyncUtils.setFromJson(data.getString(9), CLIENT_EMAILS);
    Set<Site> sites = JAsyncUtils.setFromJson(data.getString(10), CONTACT_SITES);
    Set<Messenger> messengers = JAsyncUtils.setFromJson(data.getString(11), CLIENT_MESSENGERS);

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
