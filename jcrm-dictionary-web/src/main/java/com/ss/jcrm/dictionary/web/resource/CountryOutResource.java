package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.web.resources.RestResource;
import org.jetbrains.annotations.NotNull;

public record CountryOutResource(
    long id,
    @NotNull String name,
    @NotNull String flagCode,
    @NotNull String phoneCode
) implements RestResource {

    public static @NotNull CountryOutResource from(@NotNull Country country) {
        return new CountryOutResource(
            country.getId(),
            country.getName(),
            country.getFlagCode(),
            country.getPhoneCode()
        );
    }
}
