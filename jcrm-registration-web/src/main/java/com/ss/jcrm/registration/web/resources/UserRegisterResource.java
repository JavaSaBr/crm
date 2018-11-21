package com.ss.jcrm.registration.web.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterResource {

    private final String name;
    private final String password;
}
