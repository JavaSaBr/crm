package com.ss.jcrm.security.web.resource;

import com.ss.jcrm.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizedResource<R> {

    private final User user;
    private final R resource;
}
