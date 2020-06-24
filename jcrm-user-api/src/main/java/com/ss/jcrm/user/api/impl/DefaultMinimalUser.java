package com.ss.jcrm.user.api.impl;

import com.ss.jcrm.security.AccessRole;
import com.ss.jcrm.user.api.MinimalUser;
import lombok.*;

import java.util.Set;

@Value
@EqualsAndHashCode(of = "id")
public class DefaultMinimalUser implements MinimalUser {

    long id;
    long organizationId;

    String email;

    Set<AccessRole> roles;
}
