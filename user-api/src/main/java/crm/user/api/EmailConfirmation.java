package crm.user.api;

import com.ss.jcrm.dao.UniqEntity;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface EmailConfirmation extends UniqEntity {
    @NotNull String code();
    @NotNull String email();
    @NotNull Instant expiration();
}
