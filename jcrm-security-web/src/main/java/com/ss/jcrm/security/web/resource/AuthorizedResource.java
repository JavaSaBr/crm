package com.ss.jcrm.security.web.resource;

import com.ss.jcrm.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class AuthorizedResource<R> {

    private final User user;
    private final R resource;
}
