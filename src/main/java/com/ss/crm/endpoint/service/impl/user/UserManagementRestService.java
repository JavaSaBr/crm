package com.ss.crm.endpoint.service.impl.user;


import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ss.crm.db.entity.impl.token.AccessTokenEntity;
import com.ss.crm.endpoint.service.BaseRestService;
import com.ss.crm.security.CrmUser;
import com.ss.crm.service.AccessTokenService;
import com.ss.crm.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The REST controller to work with users.
 *
 * @author JavaSaBr
 */
@Service
public class UserManagementRestService extends BaseRestService {

    public static final int MIN_FIRST_NAME_LENGTH = 1;
    public static final int MAX_FIRST_NAME_LENGTH = 25;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 25;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 25;

    @NotNull
    protected final UserService userService;

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

    @RequestMapping(
            value = "/authenticate",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody @NotNull final UserCredentialsParams params) {

        final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(params.getUsername(),
                params.getPassword());

        final Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (final BadCredentialsException e) {
            return badRequest().body(e.getLocalizedMessage());
        }

        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        final Object principal = authentication.getPrincipal();

        if (!(principal instanceof CrmUser)) {
            return badRequest().body("Can't create a crm user.");
        }

        final CrmUser crmUser = (CrmUser) principal;
        final AccessTokenEntity newToken = accessTokenService.createNewToken(crmUser.getUser());
        final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("token", newToken.getToken());

        return ok(objectNode.toString());
    }
}
