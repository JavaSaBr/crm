package com.ss.crm.service.impl;

import com.ss.crm.db.entity.impl.RoleEntity;
import com.ss.crm.db.entity.impl.UserEntity;
import com.ss.crm.db.repository.UserRepository;
import com.ss.crm.security.CrmUser;
import com.ss.crm.service.RoleService;
import com.ss.crm.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the {@link UserService}.
 *
 * @author JavaSaBr
 */
@Transactional
@Component("userService")
public class UserServiceImpl extends AbstractCrmService implements UserService {

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(@NotNull final UserRepository userRepository, @NotNull final RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @NotNull
    @Override
    public UserEntity create(@NotNull final String userName, @NotNull final byte[] hash, @NotNull final byte[] salt)
            throws RuntimeException {

        final List<RoleEntity> roles = new ArrayList<>(1);
        roles.add(roleService.getOrCreateRole(RoleService.ROLE_USER));

        final UserEntity entity = new UserEntity();
        entity.setName(userName);
        entity.setPassword(hash);
        entity.setPasswordSalt(salt);
        entity.setRoles(roles);

        userRepository.save(entity);

        return entity;
    }

    @NotNull
    @Override
    public CrmUser loadUserByUsername(@NotNull final String userName) throws UsernameNotFoundException {

        final UserEntity userEntity = userRepository.findByName(userName);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Bad credentials!");
        }

        final String password = new String(userEntity.getPassword());
        final List<RoleEntity> authorities = new ArrayList<>(userEntity.getRoles());

        return new CrmUser(userEntity.getName(), password, authorities, userEntity);
    }
}
