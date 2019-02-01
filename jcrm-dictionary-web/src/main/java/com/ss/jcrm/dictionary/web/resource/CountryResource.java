package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.dictionary.api.Country;
import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryResource implements RestResource {

    private String name;
    private String flagCode;
    private String phoneCode;

    public CountryResource(@NotNull Country country) {
        this(country.getName(), country.getFlagCode(), country.getPhoneCode());
    }
}
