package com.ss.crm.db.repository;

import com.ss.crm.db.entity.impl.token.AccessTokenEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The access token repository.
 *
 * @author JavaSaBr
 */
public interface AccessTokenRepository extends PagingAndSortingRepository<AccessTokenEntity, Long> {

    /**
     * Find a last access token by a user id.
     *
     * @param userId the user id.
     * @return the access token entity or null.
     */
    @Nullable
    AccessTokenEntity findOneByUserIdOrderByExpiryDesc(long userId);

    /**
     * Find an access token by token value.
     *
     * @param token the token value.
     * @return the access token or null.
     */
    @Nullable
    AccessTokenEntity findOneByToken(@NotNull String token);
}
