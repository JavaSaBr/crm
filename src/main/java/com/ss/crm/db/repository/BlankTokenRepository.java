package com.ss.crm.db.repository;

import com.ss.crm.db.entity.impl.token.BlankTokenEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The blank token repository.
 *
 * @author JavaSaBr
 */
public interface BlankTokenRepository extends PagingAndSortingRepository<BlankTokenEntity, Long> {

    /**
     * Find a blank token by a user id.
     *
     * @param userId the user id.
     * @return the blank token entity or null.
     */
    @Nullable
    BlankTokenEntity findOneByUserId(long userId);

    /**
     * Find a blank token by token value.
     *
     * @param token the token value.
     * @return the blank token entity or null.
     */
    @Nullable
    BlankTokenEntity findOneByToken(@NotNull String token);
}
