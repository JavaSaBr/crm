package user.jasync.dao;

import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.ss.jcrm.base.utils.DateUtils;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.jasync.dao.AbstractJAsyncDao;
import com.ss.jcrm.jasync.function.JAsyncLazyConverter;
import com.ss.jcrm.jasync.util.JAsyncUtils;
import com.ss.jcrm.security.AccessRole;
import crm.user.api.Organization;
import crm.user.api.UserGroup;
import crm.user.api.dao.OrganizationDao;
import crm.user.api.dao.UserGroupDao;
import crm.user.api.impl.DefaultUserGroup;
import com.ss.rlib.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncUserGroupDao extends AbstractJAsyncDao<UserGroup> implements UserGroupDao {

  private static final String FIELD_LIST = """
      "id", "name", "organization_id", "roles", "modified", "created", "version"
      """;

  private static final String Q_INSERT = """
      insert into "${schema}"."user_group" ("name", "organization_id", "roles", "modified", "created") 
      values (?, ?, ?, ?, ?) returning id
      """;

  private static final String Q_SELECT_ALL_BY_ORG = """
      select ${field-list} from "${schema}"."user_group" where "organization_id" = ?
      """;

  private static final String Q_SELECT_BY_ID = """
      select ${field-list} from "${schema}"."user_group" where "id" = ?
      """;

  private static final String Q_SELECT_BY_ID_AND_ORG = """
      select ${field-list} from "${schema}"."user_group" where "id" = ? and "organization_id" = ?
      """;

  private static final String Q_SELECT_BY_IDS_AND_ORG = """
      select ${field-list} from "${schema}"."user_group" where "id" in (${id-list}) AND "organization_id" = ?
      """;

  private static final String Q_COUNT_BY_NAME_AND_ORG = """
      select count("id") from "${schema}"."user_group" where "name" = ? and "organization_id" = ?
      """;

  private static final String Q_COUNT_BY_ORG = """
      select count("id") from "${schema}"."user_group" where "organization_id" = ?
      """;

  private static final String Q_SEARCH_BY_NAME_AND_ORG = """
      select ${field-list} from "${schema}"."user_group" where "name" ilike (?) AND "organization_id" = ?
      """;

  private static final String Q_UPDATE = """
      update "${schema}"."user_group" set "name" = ?, "roles" = ?, "version" = ?, "modified" = ? where "id" = ? and "version" = ?
      """;

  private static final String Q_SELECT_PAGE_BY_ORG = """
      select ${field-list} from "${schema}"."user_group" where "organization_id" = ? order by "id" limit ? offset ?
      """;

  @NotNull String querySelectById;
  @NotNull String querySelectByIdAndOrg;
  @NotNull String querySelectByIdsAndOrg;
  @NotNull String querySelectAllByOrg;
  @NotNull String queryInsert;
  @NotNull String queryCountByNameAndOrg;
  @NotNull String queryCountByOrg;
  @NotNull String querySearchByNameAndOrg;
  @NotNull String queryPageByOrg;
  @NotNull String queryUpdate;

  @NotNull OrganizationDao organizationDao;

  public JAsyncUserGroupDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool,
      @NotNull String schema,
      @NotNull OrganizationDao organizationDao) {
    super(connectionPool, schema, FIELD_LIST);
    this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
    this.querySelectByIdAndOrg = prepareQuery(Q_SELECT_BY_ID_AND_ORG);
    this.querySelectByIdsAndOrg = prepareQuery(Q_SELECT_BY_IDS_AND_ORG);
    this.querySelectAllByOrg = prepareQuery(Q_SELECT_ALL_BY_ORG);
    this.queryInsert = prepareQuery(Q_INSERT);
    this.queryCountByNameAndOrg = prepareQuery(Q_COUNT_BY_NAME_AND_ORG);
    this.queryCountByOrg = prepareQuery(Q_COUNT_BY_ORG);
    this.querySearchByNameAndOrg = prepareQuery(Q_SEARCH_BY_NAME_AND_ORG);
    this.queryPageByOrg = prepareQuery(Q_SELECT_PAGE_BY_ORG);
    this.queryUpdate = prepareQuery(Q_UPDATE);
    this.organizationDao = organizationDao;
  }

  @Override
  public @NotNull Mono<UserGroup> create(
      @NotNull String name,
      @NotNull Set<AccessRole> roles,
      @NotNull Organization organization) {

    var created = Instant.now();
    var createdDateTime = DateUtils.toLocalDateTime(created);
    var args = Arrays.asList(
        name,
        organization.id(),
        JAsyncUtils.idsToJson(roles),
        createdDateTime,
        createdDateTime);

    return insert(queryInsert, args)
        .map(id -> new DefaultUserGroup(id, name, roles, organization, created, created, 0));
  }

  @Override
  public @NotNull Mono<UserGroup> update(@NotNull UserGroup userGroup) {
    userGroup.modified(Instant.now());

    var args = Arrays.asList(
        StringUtils.emptyIfNull(userGroup.name()),
        JAsyncUtils.idsToJson(userGroup.roles()),
        userGroup.version() + 1,
        DateUtils.toLocalDateTime(userGroup.modified()),
        userGroup.id(),
        userGroup.version());

    return update(queryUpdate, args, userGroup);
  }

  @Override
  public @NotNull Mono<@Nullable UserGroup> findById(long id) {
    return selectAsync(querySelectById, id, converter());
  }

  @Override
  public @NotNull Flux<UserGroup> findAll(@NotNull Organization organization) {
    return selectAllAsync(querySelectAllByOrg, organization.id(), converter());
  }

  @Override
  public @NotNull Mono<Boolean> existByName(@NotNull String name, long organizationId) {
    return count(queryCountByNameAndOrg, List.of(name, organizationId))
        .map(count -> count > 0);
  }

  @Override
  public @NotNull Flux<UserGroup> searchByName(@NotNull String name, long organizationId) {
    var pattern = "%" + name + "%";
    return selectAllAsync(querySearchByNameAndOrg, List.of(pattern, organizationId), converter());
  }

  @Override
  public @NotNull Mono<EntityPage<UserGroup>> findPageByOrganization(long offset, long size, long organizationId) {
    return selectAllAsync(queryPageByOrg, List.of(organizationId, size, offset), converter())
        .collectList()
        .zipWith(count(queryCountByOrg, organizationId), EntityPage::new);
  }

  @Override
  public @NotNull Mono<UserGroup> findByIdAndOrganization(long id, long organizationId) {
    return selectAsync(querySelectByIdAndOrg, List.of(id, organizationId), converter());
  }

  @Override
  public @NotNull Flux<UserGroup> findByIdsAndOrganization(long @Nullable [] ids, long organizationId) {

    if (ids == null || ids.length == 0) {
      return Flux.empty();
    } else if (ids.length == 1) {
      return selectAllAsync(querySelectByIdAndOrg, List.of(ids[0], organizationId), converter());
    }

    var args = new ArrayList<>(ids.length + 1);

    for (var id : ids) {
      args.add(id);
    }

    args.add(organizationId);

    return selectAllAsync(injectIdList(querySelectByIdsAndOrg, ids), args, converter());
  }

  private @NotNull JAsyncLazyConverter<@NotNull JAsyncUserGroupDao, @NotNull UserGroup> converter() {
    return JAsyncUserGroupDao::toUserGroup;
  }

  private @NotNull Mono<UserGroup> toUserGroup(@NotNull RowData data) {

    // "id", "name", "organization_id", "roles", "modified", "created", "version"
    var id = notNull(data.getLong(0));
    var name = notNull(data.getString(1));
    var orgId = notNull(data.getLong(2));
    var roles = JAsyncUtils.fromJsonIds(data.getString(3), AccessRole::require);
    var modified = notNull(DateUtils.toUtcInstant(data.getAs(4)));
    var created = notNull(DateUtils.toUtcInstant(data.getAs(5)));
    var version = notNull(data.getInt(6));

    return organizationDao
        .requireById(orgId)
        .map(organization -> new DefaultUserGroup(id, name, roles, organization, created, modified, version));
  }
}
