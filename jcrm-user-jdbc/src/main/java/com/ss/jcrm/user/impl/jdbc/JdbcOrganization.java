package com.ss.jcrm.user.impl.jdbc;

import com.ss.jcrm.user.api.Organization;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class JdbcOrganization implements Organization {

    private final String name;
    private final long id;
}
