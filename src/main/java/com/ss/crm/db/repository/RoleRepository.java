package com.ss.crm.db.repository;

import com.ss.crm.db.entity.impl.RoleEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The role repository.
 *
 * @author JavaSaBr
 */
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Long> {

    /**
     * Find a role by a name.
     *
     * @param name the role name.
     * @return the role entity or null.
     */
    @Nullable
    RoleEntity findByName(@NotNull String name);
}
