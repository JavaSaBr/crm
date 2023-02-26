package crm.user.jasync.dao;

import static crm.base.util.DateUtils.toUtcInstant;
import static com.ss.rlib.common.util.ObjectUtils.notNull;

import com.github.jasync.sql.db.ConcreteConnection;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.pool.ConnectionPool;
import crm.base.util.DateUtils;
import crm.jasync.dao.AbstractJAsyncDao;
import crm.jasync.function.JAsyncConverter;
import crm.user.api.EmailConfirmation;
import crm.user.api.dao.EmailConfirmationDao;
import crm.user.api.impl.DefaultEmailConfirmation;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JAsyncEmailConfirmationDao extends AbstractJAsyncDao<EmailConfirmation> implements EmailConfirmationDao {

  private static final String FIELD_LIST = """
      "id", "code", "email", "expiration"
      """;

  private static final String Q_SELECT_BY_ID = """
      select ${field-list} from "${schema}"."email_confirmation" where "id" = ?
      """;

  private static final String Q_SELECT_BY_CODE_AND_EMAIL = """
      select ${field-list} from "${schema}"."email_confirmation" where "code" = ? and "email" = ?
      """;

  private static final String Q_INSERT = """
      insert into "${schema}"."email_confirmation" ("code", "email", "expiration") values (?, ?, ?) returning id
      """;

  private static final String Q_DELETE_BY_ID = """
      delete from "${schema}"."email_confirmation" where "id" = ?
      """;

  @NotNull String querySelectById;
  @NotNull String querySelectByCodeAndEmail;
  @NotNull String queryInsert;
  @NotNull String queryDeleteById;

  public JAsyncEmailConfirmationDao(
      @NotNull ConnectionPool<? extends ConcreteConnection> connectionPool, @NotNull String schema) {
    super(connectionPool, schema, FIELD_LIST);
    this.querySelectById = prepareQuery(Q_SELECT_BY_ID);
    this.querySelectByCodeAndEmail = prepareQuery(Q_SELECT_BY_CODE_AND_EMAIL);
    this.queryInsert = prepareQuery(Q_INSERT);
    this.queryDeleteById = prepareQuery(Q_DELETE_BY_ID);
  }

  @Override
  public @NotNull Mono<EmailConfirmation> create(
      @NotNull String code,
      @NotNull String email,
      @NotNull Instant expiration) {
    var args = List.of(code, email, DateUtils.toLocalDateTime(expiration));
    return insert(queryInsert, args)
        .map(id -> new DefaultEmailConfirmation(id, code, email, expiration));
  }

  @Override
  public @NotNull Mono<EmailConfirmation> findById(long id) {
    return select(querySelectById, id, converter());
  }

  @Override
  public @NotNull Mono<EmailConfirmation> findByEmailAndCode(@NotNull String email, @NotNull String code) {
    var args = List.of(code, email);
    return select(querySelectByCodeAndEmail, args, converter());
  }

  @Override
  public @NotNull Mono<Boolean> deleteById(long id) {
    return delete(queryDeleteById, id);
  }

  private @NotNull JAsyncConverter<@NotNull JAsyncEmailConfirmationDao, @NotNull EmailConfirmation> converter() {
    return JAsyncEmailConfirmationDao::toEmailConformation;
  }

  private @NotNull EmailConfirmation toEmailConformation(@NotNull RowData data) {
    return new DefaultEmailConfirmation(
        notNull(data.getLong(0)),                 // id
        notNull(data.getString(1)),               // code
        notNull(data.getString(2)),               // email
        notNull(toUtcInstant(data.getDate(3))));  // expiration
  }
}
