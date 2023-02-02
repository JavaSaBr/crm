package crm.user.api;

import com.ss.jcrm.dao.ChangeTrackedEntity;
import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.security.WithAccessRoles;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface UserGroup extends ChangeTrackedEntity, NamedUniqEntity, VersionedUniqEntity, WithAccessRoles,
    WithOrganization {

  UserGroup name(@NotNull String name);

  @NotNull UserGroup roles(@NotNull Set<AccessRole> roles);
}
