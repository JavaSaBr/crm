package com.ss.crm.security.provider;

import com.ss.crm.security.CrmUser;
import com.ss.crm.service.UserService;
import com.ss.crm.util.PasswordUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * The JDBC authentication provider.
 *
 * @author JavaSaBr
 */
@Service("jdbcAuthenticationProvider")
public class JdbcAuthenticationProvider implements AuthenticationProvider {

    @NotNull
    private final UserService userService;

    @Autowired
    public JdbcAuthenticationProvider(@NotNull final UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(@NotNull final Authentication authentication) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        final String name = token.getName();
        final String credentials = (String) authentication.getCredentials();

        final CrmUser crmUser = userService.loadUserByUsername(name);
        final byte[] passwordBytes = crmUser.getPasswordBytes();
        final byte[] passwordSalt = crmUser.getPasswordSalt();

        if (!PasswordUtil.isExpectedPassword(credentials.toCharArray(), passwordSalt, passwordBytes)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        final UsernamePasswordAuthenticationToken result =
                new UsernamePasswordAuthenticationToken(crmUser, credentials, crmUser.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(@NotNull final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
