package crm.user.api;

import crm.dao.UniqEntity;
import crm.security.AccessRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MinimalUser extends UniqEntity {
    @NotNull String email();
    long organizationId();
    @NotNull Set<AccessRole> roles();
}
