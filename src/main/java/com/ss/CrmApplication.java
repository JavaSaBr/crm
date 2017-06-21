package com.ss;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class CrmApplication {

    public static void main(@NotNull final String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

    /**
     * The name of this application.
     */
    @Value("${application.name}")
    private String name;

    /**
     * @return the name of this application.
     */
    @NotNull
    public String getName() {
        return name;
    }

    @RequestMapping(value = "/dashboard")
    public String dashboard() {
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "index";
    }
}
