package com.ss.crm.service.impl;

import static com.ss.rlib.util.array.ArrayFactory.asArray;
import static java.util.stream.Collectors.toList;
import com.ss.CrmApplication;
import com.ss.crm.db.entity.impl.RoleEntity;
import com.ss.crm.db.entity.impl.user.AdminEntity;
import com.ss.crm.db.entity.impl.user.UserEntity;
import com.ss.crm.db.repository.UserRepository;
import com.ss.crm.security.CrmUser;
import com.ss.crm.service.RoleService;
import com.ss.crm.service.UserService;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.StringUtils;
import com.ss.rlib.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The implementation of the {@link UserService}.
 *
 * @author JavaSaBr
 */
@Transactional
@Component("userService")
public class UserServiceImpl extends AbstractCrmService implements UserService {

    @NotNull
    private static final byte[] EMPTY_HASH = new byte[0];

    @NotNull
    private final CrmApplication crmApplication;

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final RoleService roleService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(@NotNull final CrmApplication crmApplication, @NotNull final UserRepository userRepository,
                           @NotNull final RoleService roleService, @NotNull final EntityManager entityManager) {
        this.crmApplication = crmApplication;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.entityManager = entityManager;
    }

    @NotNull
    @Override
    public <T extends UserEntity> T create(@NotNull final Class<T> type, @NotNull final String userName,
                                           @NotNull final Array<String> roleNames, @NotNull final byte[] hash,
                                           @NotNull final byte[] salt, @Nullable final Consumer<T> setter)
            throws RuntimeException {

        final List<RoleEntity> roles = roleNames.stream()
                .map(roleService::getOrCreateRole)
                .collect(toList());

        final T entity = ClassUtils.newInstance(type);

        if (setter != null) {
            setter.accept(entity);
        }

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

    @NotNull
    @Override
    @Transactional
    public CrmUser loadAdminUser() {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<AdminEntity> criteriaQuery = builder.createQuery(AdminEntity.class);
        criteriaQuery.select(criteriaQuery.from(AdminEntity.class));

        final TypedQuery<AdminEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(1);

        AdminEntity admin;

        try {
            admin = typedQuery.getSingleResult();
        } catch (final NoResultException e) {
            final Array<String> roleNames = asArray(RoleService.ROLE_ADMIN);
            admin = create(AdminEntity.class, crmApplication.getUserAdminName(), roleNames, EMPTY_HASH, EMPTY_HASH);
        }

        if (!StringUtils.equals(admin.getName(), crmApplication.getUserAdminName())) {
            admin.setName(crmApplication.getUserAdminName());
            userRepository.save(admin);
        }

        final List<RoleEntity> authorities = new ArrayList<>(admin.getRoles());
        return new CrmUser(admin.getName(), "", authorities, admin);
    }
}
