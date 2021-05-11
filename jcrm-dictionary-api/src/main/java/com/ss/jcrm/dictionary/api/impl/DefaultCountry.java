package com.ss.jcrm.dictionary.api.impl;

import com.ss.jcrm.dictionary.api.Country;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultCountry implements Country {

    @NotNull String name;
    @NotNull String flagCode;
    @NotNull String phoneCode;

    long id;

    public DefaultCountry(@NotNull String name, @NotNull String flagCode, @NotNull String phoneCode, long id) {
        this.name = name;
        this.flagCode = flagCode;
        this.phoneCode = phoneCode;
        this.id = id;
    }
}
