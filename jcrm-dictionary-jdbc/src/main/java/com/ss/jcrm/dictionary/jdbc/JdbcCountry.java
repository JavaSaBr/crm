package com.ss.jcrm.dictionary.jdbc;

import com.ss.jcrm.dictionary.api.Country;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter(onMethod_ = @NotNull)
@EqualsAndHashCode(of = "id")
public class JdbcCountry implements Country {

    private final String name;
    private final String flagCode;
    private final String phoneCode;

    private final long id;

    public JdbcCountry(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode, long id) {
        this.name = name;
        this.flagCode = flagCode;
        this.phoneCode = phoneCode;
        this.id = id;
    }
}
