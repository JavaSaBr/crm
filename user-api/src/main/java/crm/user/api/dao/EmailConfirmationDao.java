package crm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import crm.user.api.EmailConfirmation;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.time.Instant;

public interface EmailConfirmationDao extends Dao<EmailConfirmation> {

  @NotNull Mono<EmailConfirmation> create(
      @NotNull String code, @NotNull String email, @NotNull Instant expiration);

  @NotNull Mono<EmailConfirmation> findByEmailAndCode(@NotNull String email, @NotNull String code);

  @NotNull Mono<Boolean> deleteById(long id);
}
