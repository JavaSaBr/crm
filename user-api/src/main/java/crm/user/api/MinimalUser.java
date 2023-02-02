package crm.user.api;

import com.ss.jcrm.dao.UniqEntity;
import com.ss.jcrm.security.AccessRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MinimalUser extends UniqEntity {
    @NotNull String email();
    long orgId();
    @NotNull Set<AccessRole> roles();
}
