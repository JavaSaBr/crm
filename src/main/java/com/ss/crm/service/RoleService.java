package com.ss.crm.service;

import com.ss.crm.db.entity.impl.RoleEntity;
import org.jetbrains.annotations.NotNull;

/**
 * The service to work with role entities.
 *
 * @author JavaSaBr
 */
public interface RoleService extends CrmService {

    String ROLE_USER = "USER";

    /**
     * Get or create a role by a name.
     *
     * @param name the role name.
     * @return the role.
     */
    @NotNull
    RoleEntity getOrCreateRole(@NotNull final String name);
}
