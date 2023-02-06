package crm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import crm.user.api.MinimalUser;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface MinimalUserDao extends Dao<MinimalUser> {
  @NotNull Mono<EntityPage<MinimalUser>> findPageByOrganizationAndGroup(
      long offset, long size, long organizationId, long groupId);
}
