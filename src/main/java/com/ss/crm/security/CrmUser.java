package com.ss.crm.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author JavaSaBr
 */
public class CrmUser extends User {

    /**
     * The password bytes.
     */
    @NotNull
    private final byte[] passwordBytes;

    /**
     * The password salt.
     */
    @NotNull
    private final byte[] passwordSalt;

    public CrmUser(@NotNull final String username, @NotNull final String password,
                   @NotNull final Collection<? extends GrantedAuthority> authorities,
                   @NotNull final byte[] passwordBytes, @NotNull final byte[] passwordSalt) {
        super(username, password, authorities);
        this.passwordBytes = passwordBytes;
        this.passwordSalt = passwordSalt;
    }

    public CrmUser(@NotNull final String username, @NotNull final String password, final boolean enabled,
                   final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
                   @NotNull final Collection<? extends GrantedAuthority> authorities,
                   @NotNull final byte[] passwordBytes, @NotNull final byte[] passwordSalt) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.passwordBytes = passwordBytes;
        this.passwordSalt = passwordSalt;
    }

    /**
     * @return the password bytes.
     */
    @NotNull
    public byte[] getPasswordBytes() {
        return passwordBytes;
    }

    /**
     * @return the password salt.
     */
    @NotNull
    public byte[] getPasswordSalt() {
        return passwordSalt;
    }
}
