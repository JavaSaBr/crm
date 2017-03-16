package com.ss.crm.service;

import com.ss.crm.db.entity.impl.AccessTokenEntity;
import com.ss.crm.db.entity.impl.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service to work with access token entities.
 *
 * @author JavaSaBr
 */
public interface AccessTokenService extends CrmService {

    /**
     * Get a last access token for the user.
     *
     * @param user the user.
     * @return the last access token or null.
     */
    @Nullable
    AccessTokenEntity getLastToken(@NotNull UserEntity user);

    /**
     * Create a new access token for the user.
     *
     * @param user the user.
     * @return the new access token.
     */
    @NotNull
    AccessTokenEntity createNewToken(@NotNull UserEntity user);

    /**
     * Find an user entity by token value.
     *
     * @param token the token value.
     * @return the user entity or null.
     */
    @Nullable
    @Transactional
    UserEntity findUserByToken(@NotNull String token);
}
