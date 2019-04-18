package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.web.resources.RestResource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class UserOutResource implements RestResource {

    private final String name;
    private final String firstName;
    private final String secondName;
    private final String phoneNumber;

    private long id;

    public UserOutResource(@NotNull User user) {
        this.name = user.getName();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
        this.phoneNumber = user.getPhoneNumber();
        this.id = user.getId();
    }
}
