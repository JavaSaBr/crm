package com.ss.jcrm.registration.web.resources;

import com.ss.jcrm.user.api.User;
import com.ss.jcrm.web.resources.RestResource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class MinimalUserOutResource implements RestResource {

    private final String email;
    private final String firstName;
    private final String secondName;
    private final String thirdName;
    private final String phoneNumber;

    private long id;

    public MinimalUserOutResource(@NotNull User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
        this.thirdName = user.getThirdName();
        this.phoneNumber = user.getPhoneNumber();
        this.id = user.getId();
    }
}
