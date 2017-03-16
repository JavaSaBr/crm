package com.ss.crm.security;

import com.ss.crm.db.entity.impl.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * The CRM user.
 *
 * @author JavaSaBr
 */
public class CrmUser extends User {

    /**
     * The user entity from DB.
     */
    @NotNull
    private final UserEntity user;

    public CrmUser(@NotNull final String username, @NotNull final String password,
                   @NotNull final Collection<? extends GrantedAuthority> authorities,
                   @NotNull final UserEntity user) {
        super(username, password, authorities);
        this.user = user;
    }

    public CrmUser(@NotNull final String username, @NotNull final String password, final boolean enabled,
                   final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
                   @NotNull final Collection<? extends GrantedAuthority> authorities,
                   @NotNull final UserEntity user) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
    }

    /**
     * @return the password bytes.
     */
    @NotNull
    public byte[] getPasswordBytes() {
        return user.getPassword();
    }

    /**
     * @return the password salt.
     */
    @NotNull
    public byte[] getPasswordSalt() {
        return user.getPasswordSalt();
    }

    /**
     * @return the user entity.
     */
    @NotNull
    public UserEntity getUser() {
        return user;
    }
}
