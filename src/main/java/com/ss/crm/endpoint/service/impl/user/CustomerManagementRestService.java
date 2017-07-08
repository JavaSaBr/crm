package com.ss.crm.endpoint.service.impl.user;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import com.ss.CrmApplication;
import com.ss.crm.Routes;
import com.ss.crm.db.entity.impl.token.CustomerRegisterBlankTokenEntity;
import com.ss.crm.db.entity.impl.user.CustomerEntity;
import com.ss.crm.service.*;
import com.ss.crm.util.PasswordUtil;
import com.ss.rlib.util.StringUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import com.ss.rlib.util.dictionary.ObjectDictionary;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.core.source32.ISAACRandom;
import org.apache.commons.text.RandomStringGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The REST controller to work with customers.
 *
 * @author JavaSaBr
 */
@Service
@RequestMapping("/" + Routes.API_CUSTOMER_MANAGEMENT)
public class CustomerManagementRestService extends UserManagementRestService {

    public static final String REGISTER = "/" + Routes.API_CUSTOMER_MANAGEMENT + "register";

    @NotNull
    private static final ThreadLocal<RandomStringGenerator> PASSWORD_GENERATOR = ThreadLocal.withInitial(() -> {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int[] seed = new int[random.nextInt(10, 50)];

        for (int i = 0; i < seed.length; i++) {
            seed[i] = random.nextBoolean() ? (int) System.currentTimeMillis() :
                    random.nextBoolean() ? (int) System.nanoTime() : random.nextInt();
        }

        final UniformRandomProvider provider = new ISAACRandom(seed);
        return new RandomStringGenerator.Builder()
                .withinRange('a', 'z')
                .usingRandom(provider::nextInt)
                .build();
    });

    @NotNull
    private static final Array<String> CUSTOMER_ROLES = ArrayFactory.asArray(RoleService.ROLE_CUSTOMER);

    @NotNull
    private final MailService mailService;

    @NotNull
    private final CrmApplication application;

    @NotNull
    private final BlankTokenService blankTokenService;

    @Autowired
    public CustomerManagementRestService(@NotNull final UserService userService,
                                         @NotNull final AuthenticationManager authenticationManager,
                                         @NotNull final AccessTokenService accessTokenService,
                                         @NotNull final MailService mailService,
                                         @NotNull final CrmApplication application,
                                         @NotNull final BlankTokenService blankTokenService) {
        super(userService, authenticationManager, accessTokenService);
        this.mailService = mailService;
        this.application = application;
        this.blankTokenService = blankTokenService;
    }

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> register(@RequestBody @NotNull final RegisterCustomerInfo info) {

        final String name = info.getName();
        final String email = info.getEmail();
        final String phoneNumber = info.getPhoneNumber();

        if (StringUtils.isEmpty(name)) {
            return badRequest().body("The first name should be not null.");
        } else if (name.length() < MIN_FIRST_NAME_LENGTH) {
            return badRequest().body("The first name should be longer than " + MIN_USERNAME_LENGTH + " characters.");
        } else if (name.length() > MAX_FIRST_NAME_LENGTH) {
            return badRequest().body("The first name should be shorter than " + MAX_USERNAME_LENGTH + " characters.");
        } else if (StringUtils.isEmpty(email)) {
            return badRequest().body("The email should be not null.");
        } else if (!StringUtils.checkEmail(email)) {
            return badRequest().body("The email isn't correct.");
        } else if (StringUtils.isEmpty(phoneNumber)) {
            return badRequest().body("The phone number should be not null.");
        }

        final RandomStringGenerator generator = PASSWORD_GENERATOR.get();
        final String password = generator.generate(10);

        final char[] chars = password.toCharArray();
        final byte[] salt = PasswordUtil.getNextSalt();
        final byte[] hash = PasswordUtil.hash(chars, salt);

        final CustomerEntity customer;
        try {

            customer = userService.create(CustomerEntity.class, name, CUSTOMER_ROLES, hash, salt, entity -> {
                entity.setPhoneNumber(phoneNumber);
                entity.setEmail(email);
            });

        } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            return badRequest().body(e);
        }

        final CustomerRegisterBlankTokenEntity newToken =
                blankTokenService.createNewToken(CustomerRegisterBlankTokenEntity.class, customer);

        final String template = mailService.template("customer-register");
        final String link = application.getHost() + "/" + Routes.BLANK_CUSTOMER_REGISTER + "/" + newToken.getToken();

        final ObjectDictionary<String, Object> objects = mailService.threadLocalContext();
        objects.put(MailService.SUBJECT, name);
        objects.put(MailService.VAR_FIRST_NAME, name);
        objects.put(MailService.VAR_APP_NAME, application.getName());
        objects.put(MailService.VAR_LOGIN, email);
        objects.put(MailService.VAR_PASSWORD, password);
        objects.put(MailService.VAR_LINK, link);
        objects.put(MailService.VAR_SUPPORT_EMAIL, application.getSupportEmail());
        objects.put(MailService.VAR_SUPPORT_PHONE_NUMBER, application.getSupportPhoneNumber());

        mailService.sendMail(email, template, objects);

        return ok().build();
    }
}
