package com.ss.crm.controller.user;

import static org.springframework.http.ResponseEntity.badRequest;
import com.ss.crm.controller.BaseController;
import com.ss.crm.service.UserService;
import com.ss.crm.util.PasswordUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rlib.util.StringUtils;

/**
 * The REST controller to work with users.
 *
 * @author JavaSaBr
 */
@Controller
@RequestMapping("/user-management")
public class UserManagementController extends BaseController {

    @NotNull
    private final UserService userService;

    @Autowired
    public UserManagementController(@NotNull final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        return ResponseEntity.ok().build();
    }
}
