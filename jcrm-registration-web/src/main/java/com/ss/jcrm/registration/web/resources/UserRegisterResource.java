package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResource implements RestResource {

    private String name;
    private String password;
    private long[] roles;
    private long organization;
}
