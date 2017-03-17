package com.ss.crm.security;

import com.ss.crm.filter.AuthenticationTokenProcessingFilter;
import com.ss.crm.filter.CsrfHeaderFilter;
import com.ss.crm.service.AccessTokenService;
import com.ss.crm.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * The security configuration.
 *
 * @author JavaSaBr
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @NotNull
    private final UserService userService;

    @NotNull
    private final AuthenticationProvider jdbcAuthenticationProvider;

    @NotNull
    private final AccessTokenService accessTokenService;

    @Autowired
    public SecurityConfiguration(@NotNull final UserService userService,
                                 @NotNull final AuthenticationProvider jdbcAuthenticationProvider,
                                 @NotNull final AccessTokenService accessTokenService) {
        this.userService = userService;
        this.jdbcAuthenticationProvider = jdbcAuthenticationProvider;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring()
                .antMatchers("/index.html", "/dashboard", "/login", "/register/**", "/")
                .antMatchers("/inline.bundle.js", "/polyfills.bundle.js", "/styles.bundle.js")
                .antMatchers("/main.bundle.js", "/vendor.bundle.js")
                .antMatchers(HttpMethod.POST, "/user-management/register/**")
                .antMatchers(HttpMethod.POST, "/user-management/authenticate/**");
    }

    @Override
    protected void configure(@NotNull final HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/index.html", "/dashboard", "/login", "/register/**", "/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthenticationTokenProcessingFilter(accessTokenService), CsrfFilter.class)
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .csrf().csrfTokenRepository(csrfTokenRepository());
    }

    @Autowired
    public void configureGlobal(@NotNull final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        auth.authenticationProvider(jdbcAuthenticationProvider);
    }

    @NotNull
    private CsrfTokenRepository csrfTokenRepository() {
        final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}