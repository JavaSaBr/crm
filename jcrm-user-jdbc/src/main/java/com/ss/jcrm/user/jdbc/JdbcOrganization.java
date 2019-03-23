package com.ss.jcrm.user.jdbc;

import com.ss.jcrm.dictionary.api.City;
import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.dictionary.api.Industry;
import com.ss.jcrm.user.api.Organization;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class JdbcOrganization implements Organization {

    private final long id;

    private volatile int version;

    private String name;
    private String zipCode;
    private String address;
    private String email;
    private String phoneNumber;

    private City city;
    private Country country;
    private Set<Industry> industries;

    public JdbcOrganization(long id, int version, @NotNull String name) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.industries = Set.of();
    }

    public JdbcOrganization(long id, int version, @NotNull String name, @NotNull Country country) {
        this(id, version, name);
        this.country = country;
    }
}
