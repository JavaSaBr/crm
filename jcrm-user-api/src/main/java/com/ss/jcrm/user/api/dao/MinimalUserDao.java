package com.ss.jcrm.user.api.dao;

import com.ss.jcrm.dao.Dao;
import com.ss.jcrm.dao.EntityPage;
import com.ss.jcrm.user.api.MinimalUser;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface MinimalUserDao extends Dao<MinimalUser> {

    MinimalUser[] EMPTY_USERS = new MinimalUser[0];

    @NotNull Mono<@NotNull EntityPage<MinimalUser>> findPageByOrgAndGroup(
        long offset,
        long size,
        long orgId,
        long groupId
    );
}
