package com.ss;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class CrmApplication {

    public static void main(@NotNull final String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

    /**
     * The name of an admin user of this application.
     */
    @Value("${application.user.admin.name}")
    private String userAdminName;

    /**
     * The password of an admin user of this application.
     */
    @Value("${application.user.admin.password}")
    private String userAdminPassword;

    /**
     * The name of this application.
     */
    @Value("${application.name}")
    private String name;

    /**
     * The host of this application.
     */
    @Value("${application.host}")
    private String host;

    /**
     * The support's email.
     */
    @Value("${application.support.email}")
    private String supportEmail;

    /**
     * The support's phone number.
     */
    @Value("${application.support.phoneNumber}")
    private String supportPhoneNumber;


    /**
     * @return the name of this application.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return the host of this application.
     */
    @NotNull
    public String getHost() {
        return host;
    }

    /**
     * @return the support's email.
     */
    @NotNull
    public String getSupportEmail() {
        return supportEmail;
    }

    /**
     * @return the support's phone number.
     */
    @NotNull
    public String getSupportPhoneNumber() {
        return supportPhoneNumber;
    }

    /**
     * @return the name of an admin user of this application..
     */
    @NotNull
    public String getUserAdminName() {
        return userAdminName;
    }

    /**
     * @return the password of an admin user of this application..
     */
    @NotNull
    public String getUserAdminPassword() {
        return userAdminPassword;
    }

    @RequestMapping(value = "/dashboard")
    public String dashboard() {
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "index";
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "index";
    }

    @RequestMapping(value = "/blank/**")
    public String blank() {
        return "index";
    }
}
