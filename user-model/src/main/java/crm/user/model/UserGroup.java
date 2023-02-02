package crm.user.model;

import com.ss.jcrm.dao.ChangeTrackedEntity;
import com.ss.jcrm.dao.NamedUniqEntity;
import com.ss.jcrm.dao.VersionedUniqEntity;
import com.ss.jcrm.security.AccessRole;
import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface UserGroup extends ChangeTrackedEntity, NamedUniqEntity, VersionedUniqEntity {

    void setName(@NotNull String name);

    @NotNull Organization getOrganization();

    @NotNull Set<AccessRole> getRoles();

    default @NotNull Stream<AccessRole> streamRoles() {
        return getRoles().stream();
    }

    void setRoles(@NotNull Set<AccessRole> roles);
}
