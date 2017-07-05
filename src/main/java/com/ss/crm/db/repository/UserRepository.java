package com.ss.crm.db.repository;

import com.ss.crm.db.entity.impl.user.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The user repository.
 *
 * @author JavaSaBr
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    /**
     * Find an user by a name.
     *
     * @param name the user name.
     * @return the user entity or null.
     */
    @Nullable
    UserEntity findByName(@NotNull String name);
}
