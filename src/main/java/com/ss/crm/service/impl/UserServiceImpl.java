package com.ss.crm.service.impl;

import com.ss.crm.db.entity.impl.UserEntity;
import com.ss.crm.db.repository.UserRepository;
import com.ss.crm.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Thr base implementation of the {@link UserService}.
 *
 * @author JavaSaBr
 */
@Transactional
@Component("userService")
public class UserServiceImpl extends AbstractCrmService implements UserService {

    @NotNull
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@NotNull final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NotNull
    @Override
    public UserEntity create(@NotNull final String userName, @NotNull final byte[] hash, @NotNull final byte[] salt)
            throws RuntimeException {

        final UserEntity entity = new UserEntity();
        entity.setName(userName);
        entity.setPassword(hash);
        entity.setPasswordSalt(salt);

        userRepository.save(entity);

        return entity;
    }
}
