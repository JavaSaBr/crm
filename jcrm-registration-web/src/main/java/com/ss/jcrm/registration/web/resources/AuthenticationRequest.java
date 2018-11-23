package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements RestResource {

    private String name;
    private String password;
}
