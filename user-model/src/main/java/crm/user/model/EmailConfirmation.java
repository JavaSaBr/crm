package crm.user.model;

import com.ss.jcrm.dao.UniqEntity;
import java.time.Instant;
import org.jetbrains.annotations.NotNull;

public interface EmailConfirmation extends UniqEntity {

    @NotNull String getCode();

    @NotNull String getEmail();

    @NotNull Instant getExpiration();
}
