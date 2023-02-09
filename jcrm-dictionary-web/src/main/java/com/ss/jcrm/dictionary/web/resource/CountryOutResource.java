package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.dictionary.api.Country;
import crm.base.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;

public record CountryOutResource(
    long id,
    @NotNull String name,
    @NotNull String flagCode,
    @NotNull String phoneCode
) implements RestResource {

    public static @NotNull CountryOutResource from(@NotNull Country country) {
        return new CountryOutResource(
            country.id(),
            country.name(),
            country.flagCode(),
            country.phoneCode()
        );
    }
}
