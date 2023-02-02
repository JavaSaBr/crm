package crm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import crm.user.api.MinimalUser;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface MinimalUserDao extends Dao<MinimalUser> {

  MinimalUser[] EMPTY_USERS = new MinimalUser[0];

  @NotNull Mono<EntityPage<MinimalUser>> findPageByOrgAndGroup(
      long offset, long size, long orgId, long groupId);
}
