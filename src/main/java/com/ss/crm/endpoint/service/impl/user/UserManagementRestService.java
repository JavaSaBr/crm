package com.ss.crm.endpoint.service.impl.user;


import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ss.crm.db.entity.impl.AccessTokenEntity;
import com.ss.crm.endpoint.service.BaseRestService;
import com.ss.crm.security.CrmUser;
import com.ss.crm.service.AccessTokenService;
import com.ss.crm.service.UserService;
import com.ss.crm.util.PasswordUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rlib.util.StringUtils;

/**
 * The REST controller to work with users.
 *
 * @author JavaSaBr
 */
@Service
@RequestMapping("/user-management")
public class UserManagementRestService extends BaseRestService {

    @NotNull
    private final UserService userService;

    @NotNull
    private final AuthenticationManager authenticationManager;

    @NotNull
    private final AccessTokenService accessTokenService;

    @Autowired
    public UserManagementRestService(@NotNull final UserService userService,
                                     @NotNull final AuthenticationManager authenticationManager,
                                     @NotNull final AccessTokenService accessTokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.accessTokenService = accessTokenService;
    }

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> register(@RequestBody @NotNull final CreateUserParams params) {

        final String name = params.getName();
        final String password = params.getPassword();

        if (StringUtils.isEmpty(name)) {
            return badRequest().body("The name should be not null.");
        } else if (StringUtils.isEmpty(password)) {
            return badRequest().body("The password should be not null.");
        }

        final char[] chars = password.toCharArray();

        final byte[] salt = PasswordUtil.getNextSalt();
        final byte[] hash = PasswordUtil.hash(chars, salt);

        try {
            userService.create(name, hash, salt);
        } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            return badRequest().body(e);
        }

        return ok().build();
    }

    @RequestMapping(
            value = "/authenticate",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> authenticate(@RequestBody @NotNull final UserCredentialsParams params) {

        final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(params.getUsername(), params.getPassword());
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        final Object principal = authentication.getPrincipal();

        if (!(principal instanceof CrmUser)) {
            return badRequest().build();
        }

        final CrmUser crmUser = (CrmUser) principal;
        final AccessTokenEntity newToken = accessTokenService.createNewToken(crmUser.getUser());
        final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("token", newToken.getToken());

        return ok(objectNode.toString());
    }
}
