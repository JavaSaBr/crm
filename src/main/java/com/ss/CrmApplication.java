package com.ss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class CrmApplication {

    @RequestMapping(value = "/dashboard")
    public String dashboard() {
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}
