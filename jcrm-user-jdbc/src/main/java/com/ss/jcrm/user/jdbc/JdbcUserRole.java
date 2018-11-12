package com.ss.jcrm.user.jdbc;

import com.ss.jcrm.user.api.UserRole;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class JdbcUserRole implements UserRole {

    private final String name;
    private final long id;
}
