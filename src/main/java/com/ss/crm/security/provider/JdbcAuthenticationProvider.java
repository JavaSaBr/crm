package com.ss.crm.security.provider;

import com.ss.CrmApplication;
import com.ss.crm.security.CrmUser;
import com.ss.crm.service.UserService;
import com.ss.crm.util.PasswordUtil;
import com.ss.rlib.util.StringUtils;
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
    private final CrmApplication crmApplication;

    @NotNull
    private final UserService userService;

    @Autowired
    public JdbcAuthenticationProvider(@NotNull final CrmApplication crmApplication, @NotNull final UserService userService) {
        this.crmApplication = crmApplication;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(@NotNull final Authentication authentication) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        final String name = token.getName();
        final String credentials = (String) authentication.getCredentials();

        final String adminName = crmApplication.getUserAdminName();
        final String adminPassword = crmApplication.getUserAdminPassword();

        CrmUser crmUser;

        if (StringUtils.equals(adminName, name) && StringUtils.equals(adminPassword, credentials)) {
            crmUser = userService.loadAdminUser();
        } else {

            crmUser = userService.loadUserByUsername(name);

            final byte[] passwordBytes = crmUser.getPasswordBytes();
            final byte[] passwordSalt = crmUser.getPasswordSalt();

            if (!PasswordUtil.isExpectedPassword(credentials.toCharArray(), passwordSalt, passwordBytes)) {
                throw new BadCredentialsException("Invalid info");
            }
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
