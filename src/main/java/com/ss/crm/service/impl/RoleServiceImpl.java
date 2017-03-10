package com.ss.crm.service.impl;

import com.ss.crm.db.entity.impl.RoleEntity;
import com.ss.crm.db.repository.RoleRepository;
import com.ss.crm.service.RoleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of the {@link RoleService}.
 *
 * @author JavaSaBr
 */
@Transactional
@Component("roleService")
public class RoleServiceImpl extends AbstractCrmService implements RoleService {

    @NotNull
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(@NotNull final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @NotNull
    @Override
    public RoleEntity getOrCreateRole(@NotNull final String name) {
        synchronized (RoleRepository.class) {

            RoleEntity role = roleRepository.findByName(name);

            if (role == null) {
                role = new RoleEntity();
                role.setName(name);
                roleRepository.save(role);
            }

            return role;
        }
    }
}
