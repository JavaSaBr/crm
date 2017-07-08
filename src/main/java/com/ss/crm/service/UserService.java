package com.ss.crm.service;

import com.ss.crm.db.entity.impl.user.UserEntity;
import com.ss.crm.security.CrmUser;
import com.ss.rlib.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Consumer;

/**
 * The service to work with user entities.
 *
 * @author JavaSaBr
 */
public interface UserService extends CrmService, UserDetailsService {

    /**
     * Create a new user in the system.
     *
     * @param type      the type of a user.
     * @param userName  the user name.
     * @param roleNames the list of requested roles.
     * @param hash      the password hash.
     * @param salt      the password salt.
     * @return the created user.
     * @throws RuntimeException if an user can't be created.
     */
    @NotNull
    default <T extends UserEntity> T create(@NotNull Class<T> type, @NotNull String userName,
                                            @NotNull Array<String> roleNames, @NotNull byte[] hash,
                                            @NotNull byte[] salt) throws RuntimeException {
        return create(type, userName, roleNames, hash, salt, null);
    }

    /**
     * Create a new user in the system.
     *
     * @param type      the type of a user.
     * @param userName  the user name.
     * @param roleNames the list of requested roles.
     * @param hash      the password hash.
     * @param salt      the password salt.
     * @param setter    the function to set other fields.
     * @return the created user.
     * @throws RuntimeException if an user can't be created.
     */
    @NotNull
    <T extends UserEntity> T create(@NotNull Class<T> type, @NotNull String userName, @NotNull Array<String> roleNames,
                                    @NotNull byte[] hash, @NotNull byte[] salt, @Nullable Consumer<T> setter)
            throws RuntimeException;

    @NotNull
    @Override
    CrmUser loadUserByUsername(@NotNull String username) throws UsernameNotFoundException;


    /**
     * Loads or creates an admin user.
     *
     * @return the admin user.
     */
    @NotNull
    CrmUser loadAdminUser();
}
