package com.ss.jcrm.security.web.resource;

import crm.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizedResource<R> {

    private final User user;
    private final R resource;
}
