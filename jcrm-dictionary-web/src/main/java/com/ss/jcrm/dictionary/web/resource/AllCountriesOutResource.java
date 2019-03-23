package com.ss.jcrm.dictionary.web.resource;

import com.ss.jcrm.web.resources.RestResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllCountriesOutResource implements RestResource {

    private CountryOutResource[] countries;

    public AllCountriesOutResource(@NotNull List<CountryOutResource> resources) {
        this.countries = resources.toArray(CountryOutResource[]::new);
    }
}
