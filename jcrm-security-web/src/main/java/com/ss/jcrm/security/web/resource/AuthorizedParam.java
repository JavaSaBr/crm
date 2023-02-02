package com.ss.jcrm.security.web.resource;

import crm.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizedParam<R> {

    private final R param;
    private final User user;

    public long getOrgId() {
        return user.organization().id();
    }
}
