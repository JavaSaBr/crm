package com.ss.jcrm.security.web.resource;

import com.ss.jcrm.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class AuthorizedParam<R> {

    private final R param;
    private final User user;

    public long getOrgId() {
        return user.getOrganization().getId();
    }
}
