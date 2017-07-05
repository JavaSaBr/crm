package com.ss.crm.service.impl;

import static java.util.stream.Collectors.toList;
import com.ss.crm.db.entity.impl.RoleEntity;
import com.ss.crm.db.entity.impl.user.UserEntity;
import com.ss.crm.db.repository.UserRepository;
import com.ss.crm.security.CrmUser;
import com.ss.crm.service.RoleService;
import com.ss.crm.service.UserService;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.array.Array;
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
    public <T extends UserEntity> T create(@NotNull final Class<T> type, @NotNull final String userName,
                                                    @NotNull final Array<String> roleNames, @NotNull final byte[] hash,
                                                    @NotNull final byte[] salt) throws RuntimeException {

        final List<RoleEntity> roles = roleNames.stream()
                .map(roleService::getOrCreateRole).collect(toList());

        final T entity = ClassUtils.newInstance(type);
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
            throw new UsernameNotFoundException("Bad info!");
        }

        final String password = new String(userEntity.getPassword());
        final List<RoleEntity> authorities = new ArrayList<>(userEntity.getRoles());

        return new CrmUser(userEntity.getName(), password, authorities, userEntity);
    }
}
