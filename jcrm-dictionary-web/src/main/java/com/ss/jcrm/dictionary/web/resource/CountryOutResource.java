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
public class CountryOutResource implements RestResource {

    private long id;

    private String name;
    private String flagCode;
    private String phoneCode;

    public CountryOutResource(@NotNull Country country) {
        this(country.getId(), country.getName(), country.getFlagCode(), country.getPhoneCode());
    }
}
