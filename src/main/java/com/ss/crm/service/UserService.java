package com.ss.crm.service;

import com.ss.crm.db.entity.impl.UserEntity;
import org.jetbrains.annotations.NotNull;

/**
 * The service to work with user entities.
 *
 * @author JavaSaBr
 */
public interface UserService extends CrmService {

    /**
     * Create a new user in the system.
     *
     * @param userName the user name.
     * @param hash     the password hash.
     * @param salt     the password salt.
     * @return the created user.
     * @throws RuntimeException if an user can't be created.
     */
    @NotNull
    UserEntity create(@NotNull final String userName, @NotNull final byte[] hash, @NotNull final byte[] salt)
            throws RuntimeException;
}
