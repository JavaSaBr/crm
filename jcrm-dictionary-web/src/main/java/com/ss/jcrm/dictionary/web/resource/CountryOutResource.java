package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.web.resources.RestResource;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@RequiredArgsConstructor
public class CountryOutResource implements RestResource {

    long id;

    @NotNull String name;
    @NotNull String flagCode;
    @NotNull String phoneCode;

    public CountryOutResource(@NotNull Country country) {
        this(country.getId(), country.getName(), country.getFlagCode(), country.getPhoneCode());
    }
}
