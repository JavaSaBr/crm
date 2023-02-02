package crm.user.model;

import com.ss.jcrm.dao.UniqEntity;
import com.ss.jcrm.security.AccessRole;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface MinimalUser extends UniqEntity {

    @NotNull String getEmail();

    long getOrganizationId();

    @NotNull Set<AccessRole> getRoles();
}
