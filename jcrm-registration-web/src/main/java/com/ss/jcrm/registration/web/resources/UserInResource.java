package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.web.resources.RestResource;
import lombok.Getter;

@Getter
public class UserInResource implements RestResource {

    private String email;
    private String firstName;
    private String secondName;
    private String thirdName;
    private String phoneNumber;

    private long id;
}
